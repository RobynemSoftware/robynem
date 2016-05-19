package com.robynem.mit.web.controller.editclub;

import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.google.GoogleHelper;
import com.robynem.mit.web.google.model.PlaceDetailsExt;
import com.robynem.mit.web.model.ContactModel;
import com.robynem.mit.web.model.authentication.PortalUserModel;
import com.robynem.mit.web.model.editclub.ClubModel;
import com.robynem.mit.web.model.editclub.OpeningInfoModel;
import com.robynem.mit.web.persistence.dao.ClubDao;
import com.robynem.mit.web.persistence.dao.MediaDao;
import com.robynem.mit.web.persistence.dao.RegistryDao;
import com.robynem.mit.web.persistence.dao.UtilsDao;
import com.robynem.mit.web.persistence.entity.*;
import com.robynem.mit.web.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.AbstractView;

import java.io.ByteArrayInputStream;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by robyn_000 on 16/04/2016.
 */
@Controller
@RequestMapping("/private/editClub")
@SessionAttributes({
        "clubGenresList",
        "daysOfWeek",
        "hoursOfDay"
})
public class EditClubController extends BaseController {

    static final Logger LOG = LoggerFactory.getLogger(EditClubController.class);

    @Autowired
    private RegistryDao registryDao;

    @Autowired
    private ClubDao clubDao;

    @Autowired
    private MediaDao mediaDao;

    @Autowired
    private UtilsDao<ClubEntity> clubDaoUtils;

    @Autowired
    private GoogleHelper googleHelper;

    @Value("${media.image.max-size}")
    private long maxImageFileSize;

    @RequestMapping(value = "/edit")
    public ModelAndView edit(@RequestParam(required = false) Long clubId, @RequestParam(required = false) boolean create, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("editClub/editClub");

        try {
            // try to retrieve clubId from request params first and then from request attribute. (It for sure comes from saveName action).
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

            modelMap.addAttribute("clubGenresList", this.registryDao.getAllClubGenres());
            modelMap.addAttribute("daysOfWeek", PortalHelper.getDaysOfWeek(TextStyle.SHORT, this.getLocale()));
            modelMap.addAttribute("hoursOfDay", PortalHelper.getHoursOfDay());

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

    @RequestMapping(value = "/showGeneralInfo")
    public ModelAndView showGeneralInfo(@RequestParam(required = false) Long clubId, ModelMap modelMap) {

        ModelAndView mv = new ModelAndView("editClub/editClubGeneral");

        try {

            ClubEntity clubEntity = null;

            ClubModel clubModel = null;

            if (clubId != null) {
                // ENTERING IN EDIT MODE
                clubEntity = this.getClubToEdit(false, clubId, EditClubTabIndex.GENERAL);

            } else if (this.getSessionAttribute(Constants.EDIT_CLUB_ID) != null) {
                // If we have band id in session, populate model.
                clubEntity = this.getClubToEdit(false, null, EditClubTabIndex.GENERAL);
            }

            clubModel = this.getClubModel(clubEntity, EditClubTabIndex.GENERAL);

            modelMap.addAttribute("clubModel", clubModel);
        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
        } finally {
            System.gc();
        }

        mv.addObject(modelMap);

        return mv;

    }

    @RequestMapping(value = "/showMedia")
    public ModelAndView showMedia(@RequestParam(required = false) Long clubId, ModelMap modelMap) {

        ModelAndView mv = new ModelAndView("editClub/editClubMedia");

        try {

            ClubEntity clubEntity = null;

            ClubModel clubModel = null;

            if (clubId != null) {
                // ENTERING IN EDIT MODE
                clubEntity = this.getClubToEdit(false, clubId, EditClubTabIndex.MEDIA);



            } else if (this.getSessionAttribute(Constants.EDIT_CLUB_ID) != null) {
                // If we have band id in session, populate model.
                clubEntity = this.getClubToEdit(false, null, EditClubTabIndex.MEDIA);
            }

            clubModel = this.getClubModel(clubEntity, EditClubTabIndex.MEDIA);

            modelMap.addAttribute("clubModel", clubModel);
        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        } finally {
            System.gc();
        }

        mv.addObject(modelMap);

        return mv;

    }

    @RequestMapping(value = "/addGalleryImage", method = RequestMethod.POST)
    public AbstractView addGalleryImage(@RequestParam MultipartFile image, ModelMap modelMap) {

        try {
            if (!image.isEmpty()) {

                if (!StringUtils.trimToEmpty(image.getContentType()).toLowerCase().contains("image")) {
                    this.addApplicationMessage(this.getMessage("profile.validation.invalid-image"),
                            MessageSeverity.FATAL, null, modelMap);
                } else if (image.getSize() > this.maxImageFileSize) {
                    this.addApplicationMessage(this.getMessage("profile.validation.image-to-large"),
                            MessageSeverity.FATAL, null, modelMap);
                } else {

                    // We need it to create a stage version if needed
                    ClubEntity clubEntity = this.getClubToEdit(true, this.getSessionAttribute(Constants.EDIT_CLUB_ID), EditClubTabIndex.MEDIA);

                    ByteArrayInputStream bais = new ByteArrayInputStream(image.getBytes());

                    Long imageId = this.mediaDao.addClubGalleryImage(clubEntity.getId(), bais);

                    modelMap.put("success", true);
                    modelMap.put("uploadedImageId", imageId);
                }
            }
        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping("/getClubStatus")
    public AbstractView getClubStatus(@RequestParam(required = false) Long clubId, ModelMap modelMap) {

        try {
            modelMap.addAttribute("clubStatus", this.getClubStatus(clubId));
        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/saveGeneralInfo", method = RequestMethod.POST)
    public ModelAndView saveGeneralInfo(@ModelAttribute ClubModel clubModel,
                             @RequestParam(required = false) List<String> emailContact,
                             @RequestParam(required = false) List<String> phoneContact,
                             @RequestParam int currentTabIndex, ModelMap modelMap) {

        ModelAndView mv = new ModelAndView();
        mv.setViewName("forward:/private/editClub/showGeneralInfo");

        modelMap.addAttribute("currentTabIndex", currentTabIndex);
        modelMap.addAttribute("success", "true");

        try {

            if (emailContact != null) {
                emailContact.stream().distinct().forEach(c -> {
                    clubModel.getEmailContacts().add(new ContactModel(c));
                });
            }

            if (phoneContact != null) {
                phoneContact.stream().distinct().forEach(c -> {
                    clubModel.getPhoneNumberContacts().add(new ContactModel(c));
                });
            }

            ClubEntity clubEntity = this.getClubToEdit(true, null, EditClubTabIndex.GENERAL);

            clubEntity.setName(StringUtils.trimToNull(clubModel.getName()));
            clubEntity.setPlaceId(StringUtils.trimToNull(clubModel.getPlaceId()));
            clubEntity.setAddressPlaceId(StringUtils.trimToNull(clubModel.getAddressPlaceId()));
            clubEntity.setAddress(StringUtils.trimToNull(clubModel.getAddress()));
            clubEntity.setTown(StringUtils.trimToNull(clubModel.getTown()));
            clubEntity.setWebSite(StringUtils.trimToNull(clubModel.getWebSite()));
            clubEntity.setDescription(StringUtils.trimToNull(clubModel.getDescription()));

            // Save genres
            clubEntity.setClubGenres(new HashSet<ClubGenreEntity>());

            if (clubModel.getGenres() != null) {
                clubModel.getGenres().stream().forEach(g -> clubEntity.getClubGenres().add(new ClubGenreEntity(Long.parseLong(g))));
            }

            // Save contacts
            clubEntity.setContacts(new HashSet<>());

            // Email
            clubModel.getEmailContacts().stream().forEach(c -> {
                clubEntity.getContacts().add(new ClubContactEntity(StringUtils.trimToNull(c.getValue()), null, clubEntity));
            });

            // Phone numbers
            clubModel.getPhoneNumberContacts().stream().forEach(c -> {
                clubEntity.getContacts().add(new ClubContactEntity(null, StringUtils.trimToNull(c.getValue()), clubEntity));
            });

            // Opening info
            clubEntity.setOpeningInfos(this.getOpeningInfoEntities(modelMap));


            this.clubDao.update(clubEntity);
        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
            modelMap.addAttribute("success", false);
        }

        mv.addObject(modelMap);

        return mv;
    }

    @RequestMapping(value = "/uploadLogoImage", method = RequestMethod.POST)
    public AbstractView uploadLogoImage(@RequestParam MultipartFile pictureFile, ModelMap modelMap) {

        try {
            if (!pictureFile.isEmpty()) {

                if (!StringUtils.trimToEmpty(pictureFile.getContentType()).toLowerCase().contains("image")) {
                    this.addApplicationMessage(this.getMessage("profile.validation.invalid-image"),
                            MessageSeverity.FATAL, null, modelMap);
                } else if (pictureFile.getSize() > this.maxImageFileSize) {
                    this.addApplicationMessage(this.getMessage("profile.validation.image-to-large"),
                            MessageSeverity.FATAL, null, modelMap);
                } else {

                    ByteArrayInputStream bais = new ByteArrayInputStream(pictureFile.getBytes());

                    ClubEntity clubToEdit = this.getClubToEdit(true, null, EditClubTabIndex.GENERAL);

                    this.mediaDao.updateClubLogoImage(clubToEdit.getId(), bais);

                    // Retrive band entity from db to get logo image id to pass to the view and let it to refresh the image on page
                    clubToEdit = this.clubDaoUtils.getByIdWithFetchedObjects(ClubEntity.class, clubToEdit.getId(), "clubLogo");

                    Long imageId = clubToEdit.getClubLogo().getId();
                    modelMap.put("success", true);
                    modelMap.put("uploadedImageId", imageId);
                }
            }
        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/addEmptyOpeningInfo", method = RequestMethod.POST)
    public ModelAndView addEmptyOpeningInfo(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("editClub/editClubOpeningInfo");

        try {
            ClubModel clubModel = new ClubModel();

            clubModel.getOpeningInfos().addAll(this.getOpeningInfoFromRequest(modelMap));
            clubModel.getOpeningInfos().add(new OpeningInfoModel(true));

            modelMap.put("clubModel", clubModel);
            modelMap.put("reloadTimePicker", true);

        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
        }

        modelAndView.addObject(modelMap);

        return modelAndView;
    }

    @RequestMapping(value = "/retrieveTownFromGooglePlaceId")
    public AbstractView retrieveTownFromGooglePlaceId(@RequestParam String placeId, ModelMap modelMap) {
        PlaceDetailsExt placeDetails = null;

        try {
            placeDetails = this.googleHelper.getPlaceDetails(placeId);
            modelMap.put("locality", placeDetails.getLocality());

        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/removeGalleryImage", method = RequestMethod.POST)
    public AbstractView removeGalleryImage(@RequestParam Long imageId, ModelMap modelMap) {

        try {
            Long clubId = null;

            // Checks actual band status
            String code = this.clubDao.getClubStatusCode(this.getSessionAttribute(Constants.EDIT_CLUB_ID));

            if (EntityStatus.PUBLISHED.toString().equals(code)) {
                /*We store the published id before createìing a stage version. After session id will be replaced.*/
                Long publishedClubId = this.getSessionAttribute(Constants.EDIT_CLUB_ID);

                // We need it to create a stage version for modification
                ClubEntity clubEntity = this.getClubToEdit(true, this.getSessionAttribute(Constants.EDIT_CLUB_ID), EditClubTabIndex.MEDIA);

                // Rtrieves the imzage id belonging to the stage version
                imageId = this.clubDao.getStageGalleryImageId(publishedClubId, imageId);

                clubId = clubEntity.getId();
            } else {
                clubId = this.getSessionAttribute(Constants.EDIT_CLUB_ID);
            }

            this.mediaDao.removeClubGalleryImage(clubId, imageId);

            modelMap.put("success", true);
        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        }

        return this.getJsonView(modelMap);
    }

    private Set<ClubOpeningInfo> getOpeningInfoEntities(ModelMap modelMap) {
        Set<ClubOpeningInfo> list = new HashSet<>();

        List<OpeningInfoModel> modelList = this.getOpeningInfoFromRequest(modelMap);

        ClubOpeningInfo entity = null;
        for (OpeningInfoModel model : modelList) {
            entity = new ClubOpeningInfo();
            entity.setOpened(model.isOpened());
            entity.setStartDay(model.getStartDay());
            entity.setEndDay(model.getEndDay());
            entity.setStartHour(PortalHelper.parseTime(model.getStartHour()));
            entity.setEndHour(PortalHelper.parseTime(model.getEndHour()));

            list.add(entity);
        }

        return list;
    }

    private List<OpeningInfoModel> getOpeningInfoFromRequest(ModelMap modelMap) {
        List<OpeningInfoModel> list = new ArrayList<>();

        String[] indexes = this.request.getParameterValues(OpeningInfoModel.INDEX_KEY);

        if (indexes != null) {
            for (String index : indexes) {
                list.add(this.getOpeningInfoFromRequest(index));
            }
        }

        return list.stream().sorted((oi1, oi2) -> oi1.getStartDay().compareTo(oi2.getStartDay())).collect(Collectors.toList());
    }

    private OpeningInfoModel getOpeningInfoFromRequest(String index) {
        OpeningInfoModel openingInfoModel = new OpeningInfoModel();



        String value = this.request.getParameter(OpeningInfoModel.START_DAY_KEY + index);

        if (StringUtils.isNotBlank(value)) {
            openingInfoModel.setStartDay(Integer.valueOf(value));
        }

        value = this.request.getParameter(OpeningInfoModel.END_DAY_KEY + index);

        if (StringUtils.isNotBlank(value)) {
            openingInfoModel.setEndDay(Integer.valueOf(value));
        }


        openingInfoModel.setStartHour(this.request.getParameter(OpeningInfoModel.START_HOUR_KEY + index));
        openingInfoModel.setEndHour(this.request.getParameter(OpeningInfoModel.END_HOUR_KEY + index));
        openingInfoModel.setOpened(this.request.getParameter(OpeningInfoModel.OPENED_KEY + index) != null);

        return openingInfoModel;
    }

    private String getClubStatus(Long clubId) {
        String clubStatus = null;

        if (clubId != null) {
            // Retrives band status
            ClubEntity clubEntity = this.clubDaoUtils.getByIdWithFetchedObjects(ClubEntity.class, clubId, "status", "publishedVersion", "stageVersions");

            if (EntityStatus.NOT_PUBLISHED.toString().equals(clubEntity.getStatus().getCode()) ||  EntityStatus.STAGE.toString().equals(clubEntity.getStatus().getCode())) {
                clubStatus = clubEntity.getStatus().getCode();
            } else if (EntityStatus.PUBLISHED.toString().equals(clubEntity.getStatus().getCode())
                    && (clubEntity.getStageVersions() != null && clubEntity.getStageVersions().size() > 0)) {
                clubStatus = EntityStatus.STAGE.toString();
            } else {
                clubStatus = clubEntity.getStatus().getCode();
            }
        }


        return clubStatus;
    }

    private ClubModel getClubModel(ClubEntity clubEntity, EditClubTabIndex tabIndex) {
        ClubModel clubModel = null;

        if (clubEntity != null) {
            clubModel = new ClubModel();

            switch (tabIndex) {
                case GENERAL:

                    clubModel.setName(clubEntity.getName());
                    clubModel.setDescription(clubEntity.getDescription());

                    if (clubEntity.getClubLogo() != null) {
                        clubModel.setLogoImageId(clubEntity.getClubLogo().getId());
                    }

                    clubModel.setWebSite(clubEntity.getWebSite());
                    clubModel.setTown(clubEntity.getTown());
                    clubModel.setPlaceId(clubEntity.getPlaceId());
                    clubModel.setAddress(clubEntity.getAddress());
                    clubModel.setAddressPlaceId(clubEntity.getAddressPlaceId());

                    if (clubEntity.getClubGenres() != null) {
                        final List<String> clubGeneres = new ArrayList<String>();
                        clubEntity.getClubGenres().stream().forEach(g -> clubGeneres.add(g.getId().toString()));

                        clubModel.setGenres(clubGeneres);
                    }

                    this.setContactsModel(clubModel, clubEntity);
                    this.setOpeningInfoModel(clubModel, clubEntity);
                    break;


                case MEDIA:
                    this.setMediaModel(clubModel, clubEntity);
                    break;
            }

        }

        return clubModel;
    }

    private void setMediaModel(ClubModel clubModel, ClubEntity clubEntity) {



        if (clubEntity.getImages() != null) {
            clubEntity.getImages().stream().forEach(i -> {
                clubModel.getMediaModel().getImageIds().add(i.getId());
            });
        }

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
