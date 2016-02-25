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
import org.springframework.web.servlet.View;
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
    private UtilsDao<BandEntity> bandUtilsDao;

    @Autowired
    private UtilsDao<UserEntity> userUtilsDao;

    @Autowired
    private UtilsDao<MusicalInstrumentEntity> instrumentsUtilsDao;

    @Value("${media.image.max-size}")
    private long maxFileSize;


    @RequestMapping(value = "/edit")
    public ModelAndView edit(@RequestParam(required = false) Long bandId, @RequestParam(required = false) boolean create, ModelMap modelMap) {
        ModelAndView mv = new ModelAndView("band/editBand");

        try {

            if (create) {
                // ENTERING IN CREATE MODE
                this.removeSessionAddtribute(Constants.EDIT_BAND_ID);
                modelMap.addAttribute("setName", true);
            } else {
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
                if (!this.canEdit(bandEntity)) {
                    this.addApplicationMessage(this.getMessage("band.user-not-allowed-to-edit"), MessageSeverity.WARNING, null, null);

                    return new ModelAndView("forward:/private/dashboard");
                }

            } else if (this.getSessionAttribute(Constants.EDIT_BAND_ID) != null) {
                // If we have band id in session, populate model.
                bandEntity = this.getBandToEdit(false, null, EditBandTabIndex.GENERAL);
            }

            bandModel = this.getBandModel(bandEntity, EditBandTabIndex.GENERAL);

            modelMap.addAttribute("bandModel", bandModel);
        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
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
                } else if (pictureFile.getSize() > this.maxFileSize) {
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

        try {
            BandEntity bandEntity = this.getBandToEdit(true, null, EditBandTabIndex.GENERAL);

            bandEntity.setName(StringUtils.trimToEmpty(bandName));

            this.bandDao.update(bandEntity);
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

                    mv.setViewName("forward:/private/editBand/showGeneralInfo");

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
            modelMap.addAttribute("success", "false");
        }

        mv.addObject(modelMap);

        return mv;
    }

    @RequestMapping(value = "/saveVideos", method = RequestMethod.POST)
    public AbstractView saveVideos(@ModelAttribute BandModel bandModel,
                                   @RequestParam(required = false) List<String> youtubeUrl,
                                   ModelMap modelMap) {
        try {

            if (youtubeUrl != null) {
                youtubeUrl.stream().forEach(u -> {
                    bandModel.getMediaModel().getYoutubeUrl().add(u);
                });
            }

            this.saveVideos(bandModel);

        } catch (Throwable e) {
            this.manageException(e, LOG, modelMap);
            modelMap.addAttribute("success", "false");
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
                users.stream().forEach(u -> items.add(new ComponentAutocompleteModel(u.getId().toString(),
                        u.getProfileImage() != null ? String.format(imageUrlPlaceholder, u.getProfileImage().getId().toString()) : null,
                        StringUtils.trimToEmpty(u.getFirstName()) + " " + StringUtils.trimToEmpty(u.getLastName()))));
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
            this.bandDao.addSelectedComponent(this.getSessionAttribute(Constants.EDIT_BAND_ID), userId);

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
                                         @RequestParam(required = false) String keyword, @RequestParam(required = false) String placeId, ModelMap modelMap) {

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
            UserEntity userEntity = this.acconDao.getUserById(userId);

            modelMap.addAttribute("town", userEntity.getTown());
            modelMap.addAttribute("biography", userEntity.getBiography());

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
                } else if (image.getSize() > this.maxFileSize) {
                    this.addApplicationMessage(this.getMessage("profile.validation.image-to-large"),
                            MessageSeverity.FATAL, null, modelMap);
                } else {

                    ByteArrayInputStream bais = new ByteArrayInputStream(image.getBytes());

                    BandEntity bandToEdit = this.getBandToEdit(true, null, EditBandTabIndex.MEDIA);

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
        });

        // Phone numbers
        bandModel.getPhoneNumberContacts().stream().forEach(c -> {
            bandEntity.getContacts().add(new BandContactEntity(null, StringUtils.trimToNull(c.getValue()), bandEntity));
        });


        this.bandDao.update(bandEntity);
    }

    private void saveVideos(BandModel bandModel) {
        Long bandId = this.getSessionAttribute(Constants.EDIT_BAND_ID);

        // Videos
        Set<VideoEntity> videos = new HashSet<VideoEntity>();

        if (bandModel.getMediaModel() != null && bandModel.getMediaModel().getYoutubeUrl() != null) {
            bandModel.getMediaModel().getYoutubeUrl().stream().forEach(u -> {
                VideoEntity videoEntity = new VideoEntity();

                videoEntity.setYoutubeUrl(u);

                videos.add(videoEntity);
            });
        }

        this.bandDao.saveBandVideos(bandId, videos);
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
                bandModel.getMediaModel().getYoutubeUrl().add(v.getYoutubeUrl());
            });
        }

        if (bandEntity.getImages() != null) {
            bandEntity.getImages().stream().forEach(i -> {
                bandModel.getMediaModel().getImageIds().add(i.getId());
            });
        }
    }

    /**
     * @param forSave           If true and BandEntity is stored in session, checks if it's a stage version.
     *                          If not, creates a stage version, store its id in session and returns it.
     * @param attributesToFetch
     * @return
     */
    private BandEntity getBandToEdit_(boolean forSave, String... attributesToFetch) {
        BandEntity bandEntity = null;

        Long bandId = this.getSessionAttribute(Constants.EDIT_BAND_ID);

        if (bandId != null) {

            /*If its for save, and attributesToFetch does not contain stageVersions, it adds it, eventually creating a new
            String array if needed.*/
            if (forSave && (
                    attributesToFetch == null
                            || !Arrays.stream(attributesToFetch).
                            filter(attr -> attr.equalsIgnoreCase("stageVersions"))
                            .findFirst().isPresent()
            )) {
                if (attributesToFetch == null) {
                    attributesToFetch = new String[1];
                }

                List temp = Arrays.asList(attributesToFetch);
                temp = new ArrayList<String>(temp);
                temp.add("stageVersions");
                temp.add("status");
                attributesToFetch = (String[]) temp.toArray(new String[temp.size()]);
            }

            //bandEntity = this.bandUtilsDao.getByIdWithFetchedObjects(BandEntity.class, bandId, attributesToFetch);
            bandEntity = this.bandDao.getBandToEdit(bandId);

            // If we need entity for save and it's not a stage version and
            if (forSave &&
                    (bandEntity.getStatus().getCode().equals(EntityStatus.PUBLISHED) || bandEntity.getStatus().getCode().equals(EntityStatus.NOT_PUBLISHED))) {

                BandEntity stageVersion = null;

                // if it hasn't a stage version, we create one else we use it.
                if ((bandEntity.getStageVersions() == null || bandEntity.getStageVersions().size() == 0)) {
                    stageVersion = new BandEntity();
                    this.bandDao.copyBandData(bandEntity.getId(), stageVersion);
                } else {
                    stageVersion = bandEntity.getStageVersions().iterator().next();
                }

                this.addSessionAttribute(Constants.EDIT_BAND_ID, stageVersion.getId());

                bandEntity = stageVersion;
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

            // If we need entity for save and it's not a stage version and
            if (forSave &&
                    (bandEntity.getStatus().getCode().equals(EntityStatus.PUBLISHED) || bandEntity.getStatus().getCode().equals(EntityStatus.NOT_PUBLISHED))) {

                BandEntity stageVersion = null;

                // if it hasn't a stage version, we create one else we use it.
                if ((bandEntity.getStageVersions() == null || bandEntity.getStageVersions().size() == 0)) {
                    stageVersion = new BandEntity();
                    this.bandDao.copyBandData(bandEntity.getId(), stageVersion);
                } else {
                    stageVersion = bandEntity.getStageVersions().iterator().next();
                }

                this.addSessionAttribute(Constants.EDIT_BAND_ID, stageVersion.getId());

                bandEntity = stageVersion;
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

}
