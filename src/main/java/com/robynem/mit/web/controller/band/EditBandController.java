package com.robynem.mit.web.controller.band;

import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.model.authentication.PortalUserModel;
import com.robynem.mit.web.model.band.BandModel;
import com.robynem.mit.web.model.band.ComponentAutocompleteModel;
import com.robynem.mit.web.model.band.ComponentModel;
import com.robynem.mit.web.model.band.ContactModel;
import com.robynem.mit.web.persistence.criteria.BandComponentsCriteria;
import com.robynem.mit.web.persistence.dao.*;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by robyn_000 on 10/01/2016.
 */
@Controller
@RequestMapping("/private/editBand")
@SessionAttributes({
        "musicGenresList",
        "instrumentsList"
})
public class EditBandController extends BaseController {

    static final Logger LOG = LoggerFactory.getLogger(EditBandController.class);

    @Autowired
    private RegistryDao registryDao;

    @Autowired
    private BandDao bandDao;

    @Autowired
    private MediaDao mediaDao;

    @Autowired
    private AccountDao acconDao;

    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private UtilsDao<BandEntity> bandUtilsDao;

    @Autowired
    private UtilsDao<UserEntity> userUtilsDao;

    @Autowired
    private UtilsDao<MusicalInstrumentEntity> instrumentsUtilsDao;

    @Value("${media.image.max-size}")
    private long maxImageFileSize;

    @Value("${media.audio.max-size}")
    private long maxAudioFileSize;


    @RequestMapping(value = "/edit")
    public ModelAndView edit(@RequestParam(required = false) Long bandId, @RequestParam(required = false) boolean create, ModelMap modelMap) {
        ModelAndView mv = new ModelAndView("band/editBand");

        try {

            // try to retrieve bandId from request params first and then from request attribute. (It for sure comes from saveName action).
            if (bandId == null) {
                bandId = this.getRequestAttribute("bandId");
            }

            if (create) {
                // ENTERING IN CREATE MODE
                this.removeSessionAddtribute(Constants.EDIT_BAND_ID);
                modelMap.addAttribute("setName", true);
            } else {
                // Checks if band exists
                if (this.bandDao.getBandById(bandId) == null) {
                    this.addApplicationMessage(this.getMessage("band.does-not-exists"), MessageSeverity.WARNING, null, null);
                    return new ModelAndView("forward:/private/dashboard");
                }

                // Checks if current user is allowed to edit this band
                if (!this.canEdit(bandId)) {
                    this.addApplicationMessage(this.getMessage("band.user-not-allowed-to-edit"), MessageSeverity.WARNING, null, null);

                    return new ModelAndView("forward:/private/dashboard");
                }

                modelMap.addAttribute("bandId", bandId);
            }

            modelMap.addAttribute("musicGenresList", this.registryDao.getAllMusicGenres());
            modelMap.addAttribute("instrumentsList", this.registryDao.getAllMusicalInstruments());
        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        mv.addObject(modelMap);
        return mv;
    }

    @RequestMapping(value = "/showGeneralInfo")
    public ModelAndView showGeneralInfo(@RequestParam(required = false) Long bandId, ModelMap modelMap) {

        ModelAndView mv = new ModelAndView("band/editBandGeneral");

        try {

            BandEntity bandEntity = null;

            BandModel bandModel = null;

            if (bandId != null) {
                // ENTERING IN EDIT MODE
                bandEntity = this.getBandToEdit(false, bandId, EditBandTabIndex.GENERAL);

                // Checks if current user is allowed to edit this band
                /*if (!this.canEdit(bandEntity)) {
                    this.addApplicationMessage(this.getMessage("band.user-not-allowed-to-edit"), MessageSeverity.WARNING, null, null);

                    return new ModelAndView("forward:/private/dashboard");
                }*/

            } else if (this.getSessionAttribute(Constants.EDIT_BAND_ID) != null) {
                // If we have band id in session, populate model.
                bandEntity = this.getBandToEdit(false, null, EditBandTabIndex.GENERAL);
            }

            bandModel = this.getBandModel(bandEntity, EditBandTabIndex.GENERAL);

            modelMap.addAttribute("bandModel", bandModel);
        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
        } finally {
            System.gc();
        }

        mv.addObject(modelMap);

        return mv;

    }

    @RequestMapping(value = "/showComponents")
    public ModelAndView showComponents(@RequestParam(required = false) Long bandId, ModelMap modelMap) {

        ModelAndView mv = new ModelAndView("band/editBandComponents");

        try {

            BandEntity bandEntity = null;

            BandModel bandModel = null;

            if (bandId != null) {
                // ENTERING IN EDIT MODE
                bandEntity = this.getBandToEdit(false, bandId, EditBandTabIndex.COMPONENTS);

                // Checks if current user is allowed to edit this band
                /*if (!this.canEdit(bandEntity)) {
                    this.addApplicationMessage(this.getMessage("band.user-not-allowed-to-edit"), MessageSeverity.WARNING, null, null);

                    return new ModelAndView("forward:/private/dashboard");
                }*/

            } else if (this.getSessionAttribute(Constants.EDIT_BAND_ID) != null) {
                // If we have band id in session, populate model.
                bandEntity = this.getBandToEdit(false, null, EditBandTabIndex.COMPONENTS);
            }

            bandModel = this.getBandModel(bandEntity, EditBandTabIndex.COMPONENTS);

            modelMap.addAttribute("bandModel", bandModel);

        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        } finally {
            System.gc();
        }

        mv.addObject(modelMap);

        return mv;

    }

    @RequestMapping(value = "/showMedia")
    public ModelAndView showMedia(@RequestParam(required = false) Long bandId, ModelMap modelMap) {

        ModelAndView mv = new ModelAndView("band/editBandMedia");

        try {

            BandEntity bandEntity = null;

            BandModel bandModel = null;

            if (bandId != null) {
                // ENTERING IN EDIT MODE
                bandEntity = this.getBandToEdit(false, bandId, EditBandTabIndex.MEDIA);

                // Checks if current user is allowed to edit this band
                /*if (!this.canEdit(bandEntity)) {
                    this.addApplicationMessage(this.getMessage("band.user-not-allowed-to-edit"), MessageSeverity.WARNING, null, null);

                    return new ModelAndView("forward:/private/dashboard");
                }*/

            } else if (this.getSessionAttribute(Constants.EDIT_BAND_ID) != null) {
                // If we have band id in session, populate model.
                bandEntity = this.getBandToEdit(false, null, EditBandTabIndex.MEDIA);
            }

            bandModel = this.getBandModel(bandEntity, EditBandTabIndex.MEDIA);

            modelMap.addAttribute("bandModel", bandModel);
        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        } finally {
            System.gc();
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
                    PortalUserModel portalUserModel = this.getAuthenticatedUser();

                    ByteArrayInputStream bais = new ByteArrayInputStream(pictureFile.getBytes());

                    BandEntity bandToEdit = this.getBandToEdit(true, null, EditBandTabIndex.GENERAL);

                    this.mediaDao.updateBandLogoImage(bandToEdit.getId(), bais);
                    ;

                    // Retrive band entity from db to get logo image id to pass to the view and let it to refresh the image on page
                    bandToEdit = this.bandUtilsDao.getByIdWithFetchedObjects(BandEntity.class, bandToEdit.getId(), "bandLogo");

                    Long imageId = bandToEdit.getBandLogo().getId();
                    modelMap.put("success", true);
                    modelMap.put("uploadedImageId", imageId);
                }
            }
        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/saveName", method = RequestMethod.POST)
    public ModelAndView saveName(@RequestParam String bandName, ModelMap modelMap) {
        ModelAndView mv = new ModelAndView();
        BandEntity bandEntity = null;
        try {
            bandEntity = this.getBandToEdit(true, null, EditBandTabIndex.GENERAL);

            bandEntity.setName(StringUtils.trimToEmpty(bandName));

            this.bandDao.update(bandEntity);

            this.addRequestAttribute("bandId", bandEntity.getId());
        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
        }

        this.setPostBack(true);

        mv.setViewName("forward:/private/editBand/edit");
        mv.addObject(modelMap);

        return mv;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute BandModel bandModel,
                             @RequestParam(required = false) List<String> emailContact,
                             @RequestParam(required = false) List<String> phoneContact,
                             @RequestParam(required = false) List<String> youtubeUrl,
                             @RequestParam int currentTabIndex, ModelMap modelMap) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("forward:/private/editBand/edit");

        modelMap.addAttribute("currentTabIndex", currentTabIndex);
        modelMap.addAttribute("success", "true");

        try {
            EditBandTabIndex tabIndex = EditBandTabIndex.fromInt(currentTabIndex);

            switch (tabIndex) {
                case GENERAL:

                    mv.setViewName("forward:/private/editBand/showGeneralInfo");

                    if (emailContact != null) {
                        emailContact.stream().distinct().forEach(c -> {
                            bandModel.getEmailContacts().add(new ContactModel(c));
                        });
                    }

                    if (phoneContact != null) {
                        phoneContact.stream().distinct().forEach(c -> {
                            bandModel.getPhoneNumberContacts().add(new ContactModel(c));
                        });
                    }

                    this.saveGeneralTab(bandModel);

                    break;

                case MEDIA:

                    /*if (true) {
                        throw new Exception("prova");
                    }

                    if (youtubeUrl != null) {
                        youtubeUrl.stream().forEach(u -> {
                            bandModel.getMediaModel().getYoutubeUrl().add(u);
                        });
                    }

                    this.saveVideos(bandModel);

                    mv.setViewName("band/videoList");*/

                    mv.setViewName("forward:/private/editBand/saveVideos");

                    break;
            }
        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
            modelMap.addAttribute("success", false);
        }

        mv.addObject(modelMap);

        return mv;
    }

    @RequestMapping(value = "/saveVideo", method = RequestMethod.POST)
    public AbstractView saveVideo(@ModelAttribute BandModel bandModel,
                                   @RequestParam(required = false) String youtubeUrl,
                                   ModelMap modelMap) {
        try {

            Long videoId = null;
            if (StringUtils.isNotBlank(youtubeUrl)) {
                // We need it to create a stage version if needed
                BandEntity bandEntity = this.getBandToEdit(true, this.getSessionAttribute(Constants.EDIT_BAND_ID), EditBandTabIndex.MEDIA);
                videoId = this.bandDao.addBandVideo(bandEntity.getId(), new VideoEntity(StringUtils.trimToEmpty(youtubeUrl)));
            }

            modelMap.addAttribute("success", true);
            modelMap.addAttribute("newVideoId", videoId);

        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
            modelMap.addAttribute("success", false);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/removeVideo", method = RequestMethod.POST)
    public AbstractView removeVideo(@ModelAttribute BandModel bandModel,
                                  @RequestParam Long videoId,
                                  ModelMap modelMap) {
        try {

            if (videoId != null) {
                // We need it to create a stage version if needed
                BandEntity bandEntity = this.getBandToEdit(true, this.getSessionAttribute(Constants.EDIT_BAND_ID), EditBandTabIndex.MEDIA);
                this.bandDao.removeBandVideo(bandEntity.getId(), videoId);
            }

            modelMap.addAttribute("success", true);

        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
            modelMap.addAttribute("success", false);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping("/filterComponentName")
    public AbstractView filterComponentNameAutocomplete(@RequestParam String term, ModelMap modelMap) {

        try {
            final String imageUrlPlaceholder = this.getContextPath() + "/media/getImage?imageId=%s&size=" + ImageSize.SMALL.toString();

            List<UserEntity> users = this.acconDao.filterMusiciansByNameForAutocomplete(this.getSessionAttribute(Constants.EDIT_BAND_ID), StringUtils.trimToEmpty(term));

            List<ComponentAutocompleteModel> items = new ArrayList<ComponentAutocompleteModel>();

            if (users != null) {
                users.stream().forEach(u -> {
                    ComponentAutocompleteModel autocompleteModel = new ComponentAutocompleteModel(u.getId().toString(),
                            u.getProfileImage() != null ? String.format(imageUrlPlaceholder, u.getProfileImage().getId().toString()) : null,
                            StringUtils.trimToEmpty(u.getFirstName()) + " " + StringUtils.trimToEmpty(u.getLastName()));

                    autocompleteModel.setEmailAddress(u.getEmailAddress());

                    items.add(autocompleteModel);
                });
            }

            modelMap.addAttribute("items", items);
        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
        }


        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/addComponentAutocomplete", method = RequestMethod.POST)
    public ModelAndView addComponentAutocomplete(@RequestParam Long userId, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("band/editBandComponentsList");

        try {
            // Add component
            this.bandDao.addSelectedComponent(this.getSessionAttribute(Constants.EDIT_BAND_ID), userId, this.getAuthenticatedUser().getId());

            BandEntity bandEntity = this.getBandToEdit(false, null, EditBandTabIndex.COMPONENTS);

            //Creates the model
            BandModel bandModel = new BandModel();
            this.setComponentsModel(bandModel, bandEntity);

            modelMap.addAttribute("bandModel", bandModel);


        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
        }

        modelAndView.addObject(modelMap);

        return modelAndView;
    }

    @RequestMapping(value = "/inviteNonRegisteredUser", method = RequestMethod.POST)
    public AbstractView inviteNonRegisteredUser(@RequestParam String emailAddress, ModelMap modelMap) {

        try {
            BandEntity bandEntity = this.bandUtilsDao.getByIdWithFetchedObjects(BandEntity.class, this.getSessionAttribute(Constants.EDIT_BAND_ID), "publishedVersion");

            if (bandEntity.getPublishedVersion() != null) {
                bandEntity = bandEntity.getPublishedVersion();
            }
            UserEntity userEntity = this.acconDao.getUserByEmailAddress(StringUtils.trimToEmpty(emailAddress));
            boolean invitationExists = this.notificationDao.externalBandInvitationExists(this.getAuthenticatedUser().getId(), emailAddress, bandEntity.getId());

            if (userEntity != null) {
                modelMap.addAttribute("emailExists", true);
            } else if (invitationExists) {
                modelMap.addAttribute("invitationExists", invitationExists);
            } else {
                modelMap.addAttribute("emailExists", false);

                this.notificationDao.sendExternalBandInvitation(this.getAuthenticatedUser().getId(), emailAddress, bandEntity.getId());

                modelMap.addAttribute("success", true);

            }

        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
            modelMap.addAttribute("success", false);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/removeComponent", method = RequestMethod.POST)
    public ModelAndView removeComponent(@RequestParam Long userId, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("band/editBandComponentsList");

        try {
            // Add component
            this.bandDao.removeComponent(this.getSessionAttribute(Constants.EDIT_BAND_ID), userId, this.getAuthenticatedUser().getId());

            BandEntity bandEntity = this.getBandToEdit(false, null, EditBandTabIndex.COMPONENTS);

            //Creates the model
            BandModel bandModel = new BandModel();
            this.setComponentsModel(bandModel, bandEntity);

            modelMap.addAttribute("bandModel", bandModel);


        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
        }

        modelAndView.addObject(modelMap);

        return modelAndView;
    }

    @RequestMapping(value = "/saveComponentInstrument", method = RequestMethod.POST)
    public AbstractView saveComponentInstrument(@RequestParam Long userId, @RequestParam Long instrumentId, @RequestParam boolean selected, ModelMap modelMap) {

        try {
            this.bandDao.saveComponentInstrument(this.getSessionAttribute(Constants.EDIT_BAND_ID), userId, instrumentId, selected);

            modelMap.addAttribute("success", true);
        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/saveComponentSinger", method = RequestMethod.POST)
     public AbstractView saveComponentSinger(@RequestParam Long bandComponentId, @RequestParam boolean selected, ModelMap modelMap) {

        try {
            this.bandDao.saveBandComponentSinger(this.getSessionAttribute(Constants.EDIT_BAND_ID), bandComponentId, selected);

            modelMap.addAttribute("success", true);
        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/saveComponentDiscJockey", method = RequestMethod.POST)
    public AbstractView saveComponentDiscJockey(@RequestParam Long bandComponentId, @RequestParam boolean selected, ModelMap modelMap) {

        try {
            this.bandDao.saveBandComponentDiscJockey(this.getSessionAttribute(Constants.EDIT_BAND_ID), bandComponentId, selected);

            modelMap.addAttribute("success", true);
        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping("/searchComponents")
    public ModelAndView searchComponents(@RequestParam(required = false) String selectedInstrument, @RequestParam(required = false) String selectedGenre,
                                         @RequestParam(required = false) String keyword, @RequestParam(required = false) String placeId,
                                         @RequestParam(required = false) boolean singer, @RequestParam(required = false) boolean dj, ModelMap modelMap) {

        ModelAndView modelAndView = new ModelAndView("band/searchComponentsResult");

        try {
            BandComponentsCriteria bandComponentsCriteria = new BandComponentsCriteria();

            if (StringUtils.isNotBlank(selectedInstrument) && StringUtils.isNumeric(selectedInstrument)) {
                bandComponentsCriteria.setInstrumentId(Long.parseLong(selectedInstrument));
            }

            if (StringUtils.isNotBlank(selectedGenre) && StringUtils.isNumeric(selectedGenre)) {
                bandComponentsCriteria.setGenreId(Long.parseLong(selectedGenre));
            }

            if (StringUtils.isNotBlank(keyword)) {
                bandComponentsCriteria.setKeyword(StringUtils.trimToEmpty(keyword));
            }

            if (StringUtils.isNotBlank(placeId)) {
                bandComponentsCriteria.setPlaceId(StringUtils.trimToEmpty(placeId));
            }

            bandComponentsCriteria.setSinger(singer);

            bandComponentsCriteria.setDj(dj);

            bandComponentsCriteria.setBandId(this.getSessionAttribute(Constants.EDIT_BAND_ID));

            List<UserEntity> searchResult = this.bandDao.searchBandComponents(bandComponentsCriteria);

            modelMap.addAttribute("searchResult", searchResult);

        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        }

        modelAndView.addObject(modelMap);
        return modelAndView;
    }

    @RequestMapping("/showMusicianDetails")
    public ModelAndView showMusicianDetails(@RequestParam Long userId, ModelMap modelMap) {

        ModelAndView modelAndView = new ModelAndView("band/musicianInfoContent");

        try {
            UserEntity userEntity = this.userUtilsDao.getByIdWithFetchedObjects(UserEntity.class, userId, "playedMusicInstruments");

            modelMap.addAttribute("town", userEntity.getTown());
            modelMap.addAttribute("biography", userEntity.getBiography());
            modelMap.addAttribute("playedInstruments", userEntity.getPlayedMusicInstruments());

        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        }

        modelAndView.addObject(modelMap);

        return  modelAndView;
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

                    ByteArrayInputStream bais = new ByteArrayInputStream(image.getBytes());

                    Long imageId = this.mediaDao.addBandGalleryImage(this.getSessionAttribute(Constants.EDIT_BAND_ID), bais);

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

    @RequestMapping(value = "/addAudio", method = RequestMethod.POST)
    public AbstractView addAudio(@RequestParam MultipartFile audio, @RequestParam String name, ModelMap modelMap) {

        try {
            if (!audio.isEmpty()) {

                if (!StringUtils.trimToEmpty(audio.getContentType()).toLowerCase().contains("mp3")) {
                    this.addApplicationMessage(this.getMessage("profile.validation.invalid-image"),
                            MessageSeverity.FATAL, null, modelMap);
                } else if (audio.getSize() > this.maxAudioFileSize) {
                    this.addApplicationMessage(this.getMessage("band.validation.audio-to-large"),
                            MessageSeverity.FATAL, null, modelMap);
                } else {

                    ByteArrayInputStream bais = new ByteArrayInputStream(audio.getBytes());

                    Long audioId = this.mediaDao.addBandAudio(this.getSessionAttribute(Constants.EDIT_BAND_ID), bais, name);

                    modelMap.put("success", true);
                    modelMap.put("uploadedAudioId", audioId);
                    modelMap.put("uploadedAudioName", name);
                }
            }
        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/removeGalleryImage", method = RequestMethod.POST)
    public AbstractView removeGalleryImage(@RequestParam Long imageId, ModelMap modelMap) {

        try {

            this.mediaDao.removeBandGalleryImage(this.getSessionAttribute(Constants.EDIT_BAND_ID), imageId);

            modelMap.put("success", true);
        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping(value = "/removeAudio", method = RequestMethod.POST)
    public AbstractView removeAudio(@RequestParam Long audioId, ModelMap modelMap) {

        try {

            this.mediaDao.removeBandAudio(this.getSessionAttribute(Constants.EDIT_BAND_ID), audioId);

            modelMap.put("success", true);
        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping("/getBandStatus")
    public AbstractView getBandStatus(@RequestParam(required = false) Long bandId, ModelMap modelMap) {

        try {
            modelMap.addAttribute("bandStatus", this.getBandStatus(bandId));
        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping("/publishBand")
    public ModelAndView publishBand(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/private/editBand/edit");

        this.addRequestAttribute("fromPublish", true);

        Map<String, Integer> errorTabMap = new HashMap<String,Integer>(){
            {
                put(PublishBandErrorCode.COMPONENTS_MISSING.toString(), 1);
                put(PublishBandErrorCode.GENRE_MISSING.toString(), 0);
                put(PublishBandErrorCode.NAME_MISSING.toString(), 0);
                put(PublishBandErrorCode.NO_ONE_COMPONENT_CONFIRMED.toString(), 1);
                put(PublishBandErrorCode.TOWN_MISSING.toString(), 0);
            }
        };

        try {
            PublishBandResult publishBandResult = this.bandDao.publishBand(this.getSessionAttribute(Constants.EDIT_BAND_ID), this.getAuthenticatedUser().getId());
            this.addRequestAttribute("bandId", this.getSessionAttribute(Constants.EDIT_BAND_ID));

            if (!publishBandResult.isSuccess()) {
                Integer currentTabIndex = errorTabMap.get(publishBandResult.getErrorCode().toString());

                this.addRequestAttribute("currentTabIndex", currentTabIndex);

                if (!PublishBandErrorCode.NOT_A_STAGE_VERSION.equals(publishBandResult.getErrorCode())) {
                    this.addApplicationMessage(this.getMessage(String.format("band.publish.validation.%s", publishBandResult.getErrorCode().toString())),
                            MessageSeverity.FATAL, null, null);
                }
            } else {
                this.addApplicationMessage(this.getMessage("band.publish.success"), MessageSeverity.INFO, null, null);
                this.addRequestAttribute("bandId", publishBandResult.getPublishedBandId());
            }
        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        } finally {
            System.gc();
        }

        return modelAndView;
    }

    private void saveGeneralTab(BandModel bandModel) {
        BandEntity bandEntity = this.getBandToEdit(true, null, EditBandTabIndex.GENERAL);

        bandEntity.setName(StringUtils.trimToNull(bandModel.getName()));
        bandEntity.setPlaceId(StringUtils.trimToNull(bandModel.getPlaceId()));
        bandEntity.setTown(StringUtils.trimToNull(bandModel.getTown()));
        bandEntity.setWebSite(StringUtils.trimToNull(bandModel.getWebSite()));
        bandEntity.setBiography(StringUtils.trimToNull(bandModel.getBiography()));

        // Save genres
        bandEntity.setMusicGenres(new HashSet<MusicGenreEntity>());

        if (bandModel.getGenres() != null) {
            bandModel.getGenres().stream().forEach(g -> bandEntity.getMusicGenres().add(new MusicGenreEntity(Long.parseLong(g))));
        }

        // Save contacts
        bandEntity.setContacts(new HashSet<BandContactEntity>());

        // Email
        bandModel.getEmailContacts().stream().forEach(c -> {
            bandEntity.getContacts().add(new BandContactEntity(StringUtils.trimToNull(c.getValue()), null, bandEntity));
            //bandEntity.getContacts().add(new BandContactEntity(StringUtils.trimToNull(c.getValue()), null));
        });

        // Phone numbers
        bandModel.getPhoneNumberContacts().stream().forEach(c -> {
            bandEntity.getContacts().add(new BandContactEntity(null, StringUtils.trimToNull(c.getValue()), bandEntity));
            //bandEntity.getContacts().add(new BandContactEntity(null, StringUtils.trimToNull(c.getValue())));
        });


        this.bandDao.update(bandEntity);
    }



    private BandModel getBandModel(BandEntity bandEntity, EditBandTabIndex tabIndex) {
        BandModel bandModel = null;

        if (bandEntity != null) {
            bandModel = new BandModel();

            switch (tabIndex) {
                case GENERAL:

                    bandModel.setName(bandEntity.getName());
                    bandModel.setBiography(bandEntity.getBiography());

                    if (bandEntity.getBandLogo() != null) {
                        bandModel.setLogoImageId(bandEntity.getBandLogo().getId());
                    }

                    bandModel.setWebSite(bandEntity.getWebSite());
                    bandModel.setTown(bandEntity.getTown());
                    bandModel.setPlaceId(bandEntity.getPlaceId());

                    if (bandEntity.getMusicGenres() != null) {
                        final List<String> musicGeneres = new ArrayList<String>();
                        bandEntity.getMusicGenres().stream().forEach(g -> musicGeneres.add(g.getId().toString()));

                        bandModel.setGenres(musicGeneres);
                    }

                    this.setContactsModel(bandModel, bandEntity);
                    break;

                case COMPONENTS:
                    this.setComponentsModel(bandModel, bandEntity);
                    break;

                case MEDIA:
                    this.setMediaModel(bandModel, bandEntity);
                    break;
            }

        }

        return bandModel;
    }

    private void setComponentsModel(BandModel bandModel, BandEntity bandEntity) {
        bandModel.setComponents(new ArrayList<ComponentModel>());

        if (bandEntity.getComponents() != null) {
            bandEntity.getComponents().stream()
                    .sorted((c1, c2) -> {
                        return c1.getUser().getFirstName().compareTo(c2.getUser().getFirstName());
                    })
                    .forEach
                            (c -> {
                                ComponentModel componentModel = new ComponentModel();

                                componentModel.setId(c.getId());
                                componentModel.setUserId(c.getUser().getId());
                                componentModel.setName(String.format("%s %s", c.getUser().getFirstName(), c.getUser().getLastName()));
                                /*Show singer flag even if user is not singer but is this band's singer.*/
                                componentModel.setSinger(c.isSinger() || c.getUser().isSinger());
                                /*Show dj flag even if user is not dj but is this band's dj.*/
                                componentModel.setDiscJockey(c.isDiscJockey() || c.getUser().isDiscJockey());
                                componentModel.setSingerSelected(c.isSinger());
                                componentModel.setDiscJockeySelected(c.isDiscJockey());
                                componentModel.setProfileImageId(c.getUser().getProfileImage() != null ? c.getUser().getProfileImage().getId().toString() : null);
                                componentModel.setConfirmed(c.isConfirmed());

                                componentModel.setInstruments(new ArrayList<ComponentModel.Instrument>());

                                // Adds instruments from the user set
                                if (c.getUser().getPlayedMusicInstruments() != null) {
                                    c.getUser().getPlayedMusicInstruments().stream().forEach(i -> {
                                        ComponentModel.Instrument instrument = componentModel.createInstrumentInstance();
                                        instrument.setName(i.getName());
                                        instrument.setId(i.getId().toString());
                                        if (c.getPlayedInstruments() != null && c.getPlayedInstruments().stream().filter(pi -> pi.getId().equals(i.getId())).findFirst().isPresent()) {
                                            instrument.setSelected(true);
                                        }

                                        componentModel.getInstruments().add(instrument);
                                    });
                                }

                                // Adds instruments set on the band but not present into the user set
                                c.getPlayedInstruments().stream().filter(i -> !c.getUser().getPlayedMusicInstruments().contains(i)).forEach(i -> {
                                    ComponentModel.Instrument instrument = componentModel.createInstrumentInstance();
                                    instrument.setName(i.getName());
                                    instrument.setId(i.getId().toString());
                                    instrument.setSelected(true);
                                    componentModel.getInstruments().add(instrument);
                                });


                                bandModel.getComponents().add(componentModel);
                            });
        }

    }

    private void setContactsModel(BandModel bandModel, BandEntity bandEntity) {
        // Email
        if (bandEntity.getContacts() != null) {
            bandEntity.getContacts().stream()
                    .filter(c -> StringUtils.isNotBlank(c.getEmailAddress()))
                    .forEach(c -> {
                        bandModel.getEmailContacts().add(new ContactModel(c.getId(), c.getEmailAddress()));
                    });
        }

        // Phone numbers
        if (bandEntity.getContacts() != null) {
            bandEntity.getContacts().stream()
                    .filter(c -> StringUtils.isNotBlank(c.getPhoneNumber()))
                    .forEach(c -> {
                        bandModel.getPhoneNumberContacts().add(new ContactModel(c.getId(), c.getPhoneNumber()));
                    });
        }
    }

    private void setMediaModel(BandModel bandModel, BandEntity bandEntity) {

        if (bandEntity.getVideos() != null) {
            bandEntity.getVideos().stream().forEach(v -> {
                bandModel.getMediaModel().getVideos().add(v);
            });
        }

        if (bandEntity.getImages() != null) {
            bandEntity.getImages().stream().forEach(i -> {
                bandModel.getMediaModel().getImageIds().add(i.getId());
            });
        }

        if (bandEntity.getAudios() != null) {
            bandEntity.getAudios().stream().forEach(a -> {
                AudioEntity audioEntity = new AudioEntity();
                audioEntity.setId(a.getId());
                audioEntity.setName(a.getName());

                bandModel.getMediaModel().getAudios().add(audioEntity);
            });
        }
    }

    /**
     * @param forSave           If true and BandEntity is stored in session, checks if it's a stage version.
     *                          If not, creates a stage version, store its id in session and returns it.
     * @param tabIndex
     * @return
     */
    private BandEntity getBandToEdit(boolean forSave, Long bandId, EditBandTabIndex tabIndex) {
        BandEntity bandEntity = null;

        if (bandId != null) {
            this.addSessionAttribute(Constants.EDIT_BAND_ID, bandId);
        } else {
            bandId = this.getSessionAttribute(Constants.EDIT_BAND_ID);
        }


        if (bandId != null) {

            bandEntity = this.getBandTabData(bandId, tabIndex);

            BandEntity stageVersion = null;

            if (bandEntity.getStageVersions() != null && bandEntity.getStageVersions().size() > 0) {

                bandEntity = this.getBandTabData(bandEntity.getStageVersions().get(0).getId(), tabIndex);

                this.addSessionAttribute(Constants.EDIT_BAND_ID, bandEntity.getId());

            } else if (forSave && bandEntity.getPublishedVersion() == null) {
                // if it hasn't a stage version, we create one and we use it.
                stageVersion = this.bandDao.createStageVersion(bandEntity.getId());

                this.addSessionAttribute(Constants.EDIT_BAND_ID, stageVersion.getId());

                // We need to retrieve TAB band data from the stage version
                bandEntity = this.getBandTabData(stageVersion.getId(), tabIndex);

            }

        } else {
            // Invoke dao to create an empty band and its stage version
            PortalUserModel portalUserModel = this.getAuthenticatedUser();
            BandEntity parentBandEntity = this.bandDao.createEmtpyBand(portalUserModel.getId());

            // Assign to controller the stave version to edit
            bandEntity = parentBandEntity.getStageVersions().iterator().next();

            // Save in session stage version band id
            this.addSessionAttribute(Constants.EDIT_BAND_ID, bandEntity.getId());
        }

        return bandEntity;
    }

    private BandEntity getBandTabData(Long bandId, EditBandTabIndex tabIndex) {
        BandEntity bandEntity = null;

        switch (tabIndex) {
            case GENERAL:
                bandEntity = this.bandDao.getBandGeneralInfo(bandId);
                break;

            case COMPONENTS:
                bandEntity = this.bandDao.getBandComponents(bandId);
                break;

            case MEDIA:
                bandEntity = this.bandDao.getBandMedia(bandId);
                break;
        }

        return bandEntity;
    }

    private boolean canEdit(BandEntity bandEntity) {
        boolean canEdit = false;

        Collection<BandOwnershipEntity> owners = bandEntity.getOwners();

        PortalUserModel currentUser = this.getAuthenticatedUser();

        Collection<String> allowedOwers = Arrays.asList(OwnerType.ADMINISTRATOR.toString(), OwnerType.OWNER.toString());

        // Checks if current user is one of allowed band's owners for editing
        canEdit = (owners.stream().filter(
                o -> o.getUser() != null
                        && currentUser.getId() != null
                        && currentUser.getId().equals(o.getUser().getId())
                        && allowedOwers.contains(o.getOwnerType().getCode()))
                .collect(Collectors.toList()).size() > 0);

        return canEdit;
    }

    private boolean canEdit(Long bandId) {
        boolean canEdit = false;

        BandEntity bandEntity = this.bandUtilsDao.getByIdWithFetchedObjects(BandEntity.class, bandId, "owners");

        Collection<BandOwnershipEntity> owners = bandEntity.getOwners();

        PortalUserModel currentUser = this.getAuthenticatedUser();

        Collection<String> allowedOwers = Arrays.asList(OwnerType.ADMINISTRATOR.toString(), OwnerType.OWNER.toString());

        // Checks if current user is one of allowed band's owners for editing
        canEdit = (owners.stream().filter(
                o -> o.getUser() != null
                        && currentUser.getId() != null
                        && currentUser.getId().equals(o.getUser().getId())
                        && allowedOwers.contains(o.getOwnerType().getCode()))
                .collect(Collectors.toList()).size() > 0);

        return canEdit;
    }

    private String getBandStatus(Long bandId) {
        String bandStatus = null;

        if (bandId != null) {
            // Retrives band status
            BandEntity bandEntity = this.bandUtilsDao.getByIdWithFetchedObjects(BandEntity.class, bandId, "status", "publishedVersion", "stageVersions");

            if (EntityStatus.NOT_PUBLISHED.toString().equals(bandEntity.getStatus().getCode()) ||  EntityStatus.STAGE.toString().equals(bandEntity.getStatus().getCode())) {
                bandStatus = bandEntity.getStatus().getCode();
            } else if (EntityStatus.PUBLISHED.toString().equals(bandEntity.getStatus().getCode())
                    && (bandEntity.getStageVersions() != null && bandEntity.getStageVersions().size() > 0)) {
                bandStatus = EntityStatus.STAGE.toString();
            } else {
                bandStatus = bandEntity.getStatus().getCode();
            }
        }


        return bandStatus;
    }

}
