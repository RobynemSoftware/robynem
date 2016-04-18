package com.robynem.mit.web.controller.editclub;

import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.model.authentication.PortalUserModel;
import com.robynem.mit.web.persistence.dao.ClubDao;
import com.robynem.mit.web.persistence.dao.UtilsDao;
import com.robynem.mit.web.persistence.entity.ClubEntity;
import com.robynem.mit.web.persistence.entity.ClubOwnershipEntity;
import com.robynem.mit.web.util.Constants;
import com.robynem.mit.web.util.EditClubTabIndex;
import com.robynem.mit.web.util.MessageSeverity;
import com.robynem.mit.web.util.OwnerType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by robyn_000 on 16/04/2016.
 */
@Controller
@RequestMapping("/private/editClub")
public class EditClubController extends BaseController {

    static final Logger LOG = LoggerFactory.getLogger(EditClubController.class);

    @Autowired
    private ClubDao clubDao;

    @Autowired
    private UtilsDao<ClubEntity> clubDaoUtils;

    @RequestMapping(value = "/edit")
    public ModelAndView edit(@RequestParam(required = false) Long clubId, @RequestParam(required = false) boolean create, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("editClub/editClub");

        try {
            // try to retrieve bandId from request params first and then from request attribute. (It for sure comes from saveName action).
            if (clubId == null) {
                clubId = this.getRequestAttribute("clubId");
            }

            if (create) {
                // ENTERING IN CREATE MODE
                this.removeSessionAddtribute(Constants.EDIT_CLUB_ID);
                modelMap.addAttribute("setName", true);
            } else {
                // Checks if band exists
                if (this.clubDao.getClubById(clubId) == null) {
                    this.addApplicationMessage(this.getMessage("club.does-not-exists"), MessageSeverity.WARNING, null, null);
                    return new ModelAndView("forward:/private/dashboard");
                }

                // Checks if current user is allowed to edit this band
                if (!this.canEdit(clubId)) {
                    this.addApplicationMessage(this.getMessage("club.user-not-allowed-to-edit"), MessageSeverity.WARNING, null, null);

                    return new ModelAndView("forward:/private/dashboard");
                }

                modelMap.addAttribute("clubId", clubId);
            }

        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        modelAndView.addObject(modelMap);

        return modelAndView;
    }

    @RequestMapping(value = "/saveName", method = RequestMethod.POST)
    public ModelAndView saveName(@RequestParam String clubName, ModelMap modelMap) {
        ModelAndView mv = new ModelAndView();
        ClubEntity clubEntity = null;
        try {
            clubEntity = this.getClubToEdit(true, null, EditClubTabIndex.GENERAL);

            clubEntity.setName(StringUtils.trimToEmpty(clubName));

            this.clubDao.update(clubEntity);

            this.addRequestAttribute("clubId", clubEntity.getId());
        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
        }

        this.setPostBack(true);

        mv.setViewName("forward:/private/editClub/edit");
        mv.addObject(modelMap);

        return mv;
    }

    /**
     * @param forSave           If true and BandEntity is stored in session, checks if it's a stage version.
     *                          If not, creates a stage version, store its id in session and returns it.
     * @param tabIndex
     * @return
     */
    private ClubEntity getClubToEdit(boolean forSave, Long clubId, EditClubTabIndex tabIndex) {
        ClubEntity clubEntity = null;

        if (clubId != null) {
            this.addSessionAttribute(Constants.EDIT_CLUB_ID, clubId);
        } else {
            clubId = this.getSessionAttribute(Constants.EDIT_CLUB_ID);
        }


        if (clubId != null) {

            clubEntity = this.getBandTabData(clubId, tabIndex);

            ClubEntity stageVersion = null;

            if (clubEntity.getStageVersions() != null && clubEntity.getStageVersions().size() > 0) {

                clubEntity = this.getBandTabData(clubEntity.getStageVersions().get(0).getId(), tabIndex);

                this.addSessionAttribute(Constants.EDIT_CLUB_ID, clubEntity.getId());

            } else if (forSave && clubEntity.getPublishedVersion() == null) {
                // if it hasn't a stage version, we create one and we use it.
                stageVersion = this.clubDao.createStageVersion(clubEntity.getId());

                this.addSessionAttribute(Constants.EDIT_CLUB_ID, stageVersion.getId());

                // We need to retrieve TAB band data from the stage version
                clubEntity = this.getBandTabData(stageVersion.getId(), tabIndex);

            }

        } else {
            // Invoke dao to create an empty club and its stage version
            PortalUserModel portalUserModel = this.getAuthenticatedUser();
            ClubEntity parentClubEntity = this.clubDao.createEmptyClub(portalUserModel.getId());

            // Assign to controller the stave version to edit
            clubEntity = parentClubEntity.getStageVersions().get(0);

            // Save in session stage version band id
            this.addSessionAttribute(Constants.EDIT_CLUB_ID, clubEntity.getId());
        }

        return clubEntity;
    }

    private ClubEntity getBandTabData(Long clubId, EditClubTabIndex tabIndex) {
        ClubEntity bandEntity = null;

        switch (tabIndex) {
            case GENERAL:
                bandEntity = this.clubDao.getClubGeneralInfo(clubId);
                break;


            case MEDIA:
                bandEntity = this.clubDao.getClubMedia(clubId);
                break;
        }

        return bandEntity;
    }


    private boolean canEdit(Long clubId) {
        boolean canEdit = false;

        ClubEntity clubEntity = this.clubDaoUtils.getByIdWithFetchedObjects(ClubEntity.class, clubId, "owners");

        Collection<ClubOwnershipEntity> owners = clubEntity.getOwners();

        PortalUserModel currentUser = this.getAuthenticatedUser();

        Collection<String> allowedOwers = Arrays.asList(OwnerType.ADMINISTRATOR.toString(), OwnerType.OWNER.toString());

        // Checks if current user is one of allowed club's owners for editing
        canEdit = (owners.stream().filter(
                o -> o.getUser() != null
                        && currentUser.getId() != null
                        && currentUser.getId().equals(o.getUser().getId())
                        && allowedOwers.contains(o.getOwnerType().getCode()))
                .collect(Collectors.toList()).size() > 0);

        return canEdit;
    }

}
