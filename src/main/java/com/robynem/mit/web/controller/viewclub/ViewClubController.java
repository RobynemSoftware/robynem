package com.robynem.mit.web.controller.viewclub;

import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.model.ContactModel;
import com.robynem.mit.web.model.authentication.PortalUserModel;
import com.robynem.mit.web.model.editclub.ClubModel;
import com.robynem.mit.web.model.editclub.OpeningInfoModel;
import com.robynem.mit.web.persistence.dao.ClubDao;
import com.robynem.mit.web.persistence.dao.UtilsDao;
import com.robynem.mit.web.persistence.entity.ClubEntity;
import com.robynem.mit.web.persistence.entity.ClubOwnershipEntity;
import com.robynem.mit.web.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by robyn_000 on 28/05/2016.
 */
@Controller
@RequestMapping("/viewClub")
public class ViewClubController extends BaseController {

    static final Logger LOG = LoggerFactory.getLogger(ViewClubController.class);

    @Autowired
    private UtilsDao<ClubEntity> clubUtilsDao;

    @Autowired
    private ClubDao clubDao;

    @RequestMapping
    public ModelAndView view(@RequestParam Long clubId, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("viewclub/viewClub");

        try {
            modelMap.addAttribute("clubId", clubId);
        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        return modelAndView;
    }

    @RequestMapping("/viewContent")
    public ModelAndView viewContent(@RequestParam Long clubId, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("viewclub/viewClubContent");

        try {
            ClubModel clubModel = new ClubModel();

            clubId = this.getClubIdToView(clubId);

            if (clubId != null) {
                this.addSessionAttribute(Constants.VIEW_BAND_ID, clubId);

                this.populateGeneralInfoModel(clubId, clubModel);
            }

            modelMap.addAttribute("clubModel", clubModel);

        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        return modelAndView;
    }

    private void populateGeneralInfoModel(Long clubId, ClubModel clubModel) {
        ClubEntity clubEntity = this.clubDao.getClubGeneralInfo(clubId);

        clubModel.setId(clubEntity.getId());
        clubModel.setName(clubEntity.getName());
        clubModel.setDescription(clubEntity.getDescription());

        if (clubEntity.getClubLogo() != null) {
            clubModel.setLogoImageId(clubEntity.getClubLogo().getId());
        }

        clubModel.setWebSite(clubEntity.getWebSite());
        clubModel.setTown(clubEntity.getTown());
        clubModel.setPlaceId(clubEntity.getPlaceId());
        clubModel.setTown(clubEntity.getTown());
        clubModel.setAddress(clubEntity.getAddress());
        clubModel.setAddressPlaceId(clubEntity.getAddressPlaceId());

        if (clubEntity.getClubGenres() != null) {
            final List<String> clubGeneres = new ArrayList<String>();
            clubEntity.getClubGenres().stream().forEach(g -> clubGeneres.add(g.getName()));

            clubModel.setGenres(clubGeneres);
        }

        // set if user is owner or admin
        if (this.getAuthenticatedUser() != null) {
            Long authenticatedUserId = this.getAuthenticatedUser().getId();

            if (clubEntity.getOwners() != null) {
                clubEntity.getOwners().stream().filter(o -> o.getUser().getId().equals(authenticatedUserId)).forEach(o -> {
                    if (OwnerType.OWNER.toString().equalsIgnoreCase(o.getOwnerType().getCode())) {
                        clubModel.setOwner(true);
                    }

                    if (OwnerType.ADMINISTRATOR.toString().equalsIgnoreCase(o.getOwnerType().getCode())) {
                        clubModel.setAdmin(true);
                    }
                });
            }
        }

        this.setContactsModel(clubModel, clubEntity);
        this.setOpeningInfoModel(clubModel, clubEntity);
    }

    private void setContactsModel(ClubModel clubModel, ClubEntity clubEntity) {
        // Email
        if (clubEntity.getContacts() != null) {
            clubEntity.getContacts().stream()
                    .filter(c -> StringUtils.isNotBlank(c.getEmailAddress()))
                    .forEach(c -> {
                        clubModel.getEmailContacts().add(new ContactModel(c.getId(), c.getEmailAddress()));
                    });
        }

        // Phone numbers
        if (clubEntity.getContacts() != null) {
            clubEntity.getContacts().stream()
                    .filter(c -> StringUtils.isNotBlank(c.getPhoneNumber()))
                    .forEach(c -> {
                        clubModel.getPhoneNumberContacts().add(new ContactModel(c.getId(), c.getPhoneNumber()));
                    });
        }
    }

    private void setOpeningInfoModel(ClubModel clubModel, ClubEntity clubEntity) {
        if (clubEntity.getOpeningInfos() != null) {
            clubEntity.getOpeningInfos().stream()
                    .sorted((oi1, oi2) -> oi1.getStartDay().compareTo(oi2.getStartDay()))
                    .forEach(oi -> {
                        OpeningInfoModel openingInfoModel = new OpeningInfoModel();

                        openingInfoModel.setId(oi.getId());

                        openingInfoModel.setStartDay(oi.getStartDay());
                        openingInfoModel.setEndDay(oi.getEndDay());
                        openingInfoModel.setOpened(oi.isOpened());

                        if (oi.getStartHour() != null) {
                            openingInfoModel.setStartHour(PortalHelper.formatTime(oi.getStartHour()));
                        }

                        if (oi.getEndHour() != null) {
                            openingInfoModel.setEndHour(PortalHelper.formatTime(oi.getEndHour()));
                        }

                        clubModel.getOpeningInfos().add(openingInfoModel);
                    });
        }
    }

    private Long getClubIdToView(Long clubId) {
        ClubEntity clubEntity = this.clubUtilsDao.getByIdWithFetchedObjects(ClubEntity.class, clubId, "status", "publishedVersion");

        String code = clubEntity.getStatus().getCode();

        if (!EntityStatus.PUBLISHED.toString().equalsIgnoreCase(code)) {

            // Checks if user is authenticated
            if (this.getAuthenticatedUser() == null) {
                clubId = null;
                this.addApplicationMessage(this.getMessage("viewclub.cannot-view"), MessageSeverity.WARNING, null, null, this.getHomeUrl());
            } else {
                // Checks if current user is owner or adminstrator
                if (!this.isOwnerOrAdmin(clubId)) {

                    clubId = null;
                    this.addApplicationMessage(this.getMessage("viewclub.cannot-view"), MessageSeverity.WARNING, null, null, this.getHomeUrl());
                }
            }
        }


        return clubId;
    }

    private boolean isOwnerOrAdmin(Long clubId) {
        boolean isOwnerOrAdmin = false;

        ClubEntity clubEntity = this.clubUtilsDao.getByIdWithFetchedObjects(ClubEntity.class, clubId, "owners");

        Collection<ClubOwnershipEntity> owners = clubEntity.getOwners();

        PortalUserModel currentUser = this.getAuthenticatedUser();

        if (currentUser != null) {
            Collection<String> allowedOwers = Arrays.asList(OwnerType.ADMINISTRATOR.toString(), OwnerType.OWNER.toString());

            // Checks if current user is one of allowed club's owners for editing
            isOwnerOrAdmin = (owners.stream().filter(
                    o -> o.getUser() != null
                            && currentUser.getId() != null
                            && currentUser.getId().equals(o.getUser().getId())
                            && allowedOwers.contains(o.getOwnerType().getCode()))
                    .collect(Collectors.toList()).size() > 0);
        }

        return isOwnerOrAdmin;
    }
}
