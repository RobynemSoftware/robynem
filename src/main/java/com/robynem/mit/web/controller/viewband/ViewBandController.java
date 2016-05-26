package com.robynem.mit.web.controller.viewband;

import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.model.authentication.PortalUserModel;
import com.robynem.mit.web.model.editband.BandModel;
import com.robynem.mit.web.model.editband.ComponentModel;
import com.robynem.mit.web.model.ContactModel;
import com.robynem.mit.web.model.viewband.AudioModel;
import com.robynem.mit.web.model.viewband.BandRequestModel;
import com.robynem.mit.web.model.viewband.GalleryModel;
import com.robynem.mit.web.model.viewband.VideosModel;
import com.robynem.mit.web.persistence.criteria.AudioCriteria;
import com.robynem.mit.web.persistence.criteria.GalleryCriteria;
import com.robynem.mit.web.persistence.criteria.VideosCriteria;
import com.robynem.mit.web.persistence.dao.BandDao;
import com.robynem.mit.web.persistence.dao.NotificationDao;
import com.robynem.mit.web.persistence.dao.UtilsDao;
import com.robynem.mit.web.persistence.entity.*;
import com.robynem.mit.web.util.Constants;
import com.robynem.mit.web.util.EntityStatus;
import com.robynem.mit.web.util.MessageSeverity;
import com.robynem.mit.web.util.OwnerType;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * Created by robyn_000 on 26/03/2016.
 */
@Controller
@RequestMapping("/viewBand")
public class ViewBandController extends BaseController {

    static final Logger LOG = LoggerFactory.getLogger(ViewBandController.class);

    @Autowired
    private BandDao bandDao;

    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private UtilsDao<BandEntity> bandUtilsDao;

    @Value("${viewband.videos.page-size}")
    private int videosPageSize;

    @Value("${viewband.gallery.page-size}")
    private int galleryPageSize;

    @Value("${viewband.audio.page-size}")
    private int audiosPageSize;

    @RequestMapping
    public ModelAndView view(@RequestParam Long bandId, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("viewband/viewBand");

        try {
            modelMap.addAttribute("bandId", bandId);
        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        return modelAndView;
    }

    @RequestMapping("/viewContent")
    public ModelAndView viewContent(@RequestParam Long bandId, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("viewband/viewBandContent");

        try {
            BandModel bandModel = new BandModel();

            bandId = this.getBandIdToView(bandId);

            if (bandId != null) {
                this.addSessionAttribute(Constants.VIEW_BAND_ID, bandId);

                this.populateGeneralInfoModel(bandId, bandModel);

                this.populateComponentsModel(bandId, bandModel);
            }

            modelMap.addAttribute("bandModel", bandModel);

        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        return modelAndView;
    }

    @RequestMapping("/getVideos")
    public ModelAndView getVideos(@RequestParam(required = false, defaultValue = "1") int currentPage, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("viewband/viewBandVideosList");

        try {
            modelMap.addAttribute("videosModel", this.getVideosModel(this.getSessionAttribute(Constants.VIEW_BAND_ID), currentPage));
        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        return modelAndView;
    }

    @RequestMapping("/getGallery")
    public ModelAndView getGallery(@RequestParam(required = false, defaultValue = "1") int currentPage, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("viewband/viewBandGalleryList");

        try {
            modelMap.addAttribute("galleryModel", this.getGalleryModel(this.getSessionAttribute(Constants.VIEW_BAND_ID), currentPage));
        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        return modelAndView;
    }

    @RequestMapping("/getAudios")
    public ModelAndView getAudios(@RequestParam(required = false, defaultValue = "1") int currentPage, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("viewband/viewBandAudioList");

        try {
            modelMap.addAttribute("audioModel", this.getAudioModel(this.getSessionAttribute(Constants.VIEW_BAND_ID), currentPage));
        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        return modelAndView;
    }

    @RequestMapping("/getActions")
    public ModelAndView getActions(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("viewband/viewBandActions");

        try {
            List<BandRequestModel> bandRequests = new ArrayList<BandRequestModel>();

            this.hasComponentInvitation(bandRequests);

            modelMap.addAttribute("bandRequests", bandRequests);
        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        return modelAndView;
    }

    @RequestMapping("/acceptDeclineComponentRequest")
    public ModelAndView acceptDeclineComponentRequest(@RequestParam boolean accept, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("forward:/viewBand/viewContent?bandId=" + this.getSessionAttribute(Constants.VIEW_BAND_ID));

        try {
            if (this.getAuthenticatedUser() != null) {

                this.bandDao.acceptDeclineBandComponentRequest(this.getSessionAttribute(Constants.VIEW_BAND_ID), this.getAuthenticatedUser().getId(), accept);

                // Retrieves owners form band to send notification
                BandEntity bandEntity = this.bandUtilsDao.getByIdWithFetchedObjects(BandEntity.class, this.getSessionAttribute(Constants.VIEW_BAND_ID), "owners");

                List<Long> notificationReceivers = new ArrayList<Long>();

                for (BandOwnershipEntity owner : bandEntity.getOwners()) {
                    notificationReceivers.add(owner.getUser().getId());
                }

                // Send notifications
                this.notificationDao.sendBandInvitationAnswer(this.getAuthenticatedUser().getId(), notificationReceivers, bandEntity.getId(), accept);

            }
        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        return modelAndView;
    }

    private boolean hasComponentInvitation(List<BandRequestModel> bandRequests) {
        boolean result = false;

        if (this.getAuthenticatedUser() != null) {

            Long bandId = this.getSessionAttribute(Constants.VIEW_BAND_ID);

            BandEntity bandEntity = this.bandUtilsDao.getByIdWithFetchedObjects(BandEntity.class, bandId, "publishedVersion", "stageVersions", "components");

            if (bandEntity.getStageVersions() != null && bandEntity.getStageVersions().stream().findFirst().isPresent()) {
                bandId = bandEntity.getStageVersions().stream().sorted((sv1, sv2) -> sv2.getId().compareTo(sv1.getId())).findFirst().get().getId();
            }

            BandComponentEntity bandComponentEntity = this.bandDao.getBandComponent(bandId, this.getAuthenticatedUser().getId());

            if (bandComponentEntity != null && !bandComponentEntity.isConfirmed()) {
                result = true;

                bandRequests.add(new BandRequestModel(BandRequestModel.COMPONENT_INVITATION));
            }
        }

        return result;
    }

    private void populateGeneralInfoModel(Long bandId, BandModel bandModel) {
        BandEntity bandEntity = this.bandDao.getBandGeneralInfo(bandId);

        bandModel.setId(bandEntity.getId());
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
            bandEntity.getMusicGenres().stream().forEach(g -> musicGeneres.add(g.getName()));

            bandModel.setGenres(musicGeneres);
        }

        // set if user is owner or admin
        if (this.getAuthenticatedUser() != null) {
            Long authenticatedUserId = this.getAuthenticatedUser().getId();

            if (bandEntity.getOwners() != null) {
                bandEntity.getOwners().stream().filter(o -> o.getUser().getId().equals(authenticatedUserId)).forEach(o -> {
                    if (OwnerType.OWNER.toString().equalsIgnoreCase(o.getOwnerType().getCode())) {
                        bandModel.setOwner(true);
                    }

                    if (OwnerType.ADMINISTRATOR.toString().equalsIgnoreCase(o.getOwnerType().getCode())) {
                        bandModel.setAdmin(true);
                    }
                });
            }
        }

        this.setContactsModel(bandModel, bandEntity);
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

    private void populateComponentsModel(Long bandId, BandModel bandModel) {
        BandEntity bandEntity = this.bandDao.getBandComponents(bandId);

        bandModel.setComponents(new ArrayList<ComponentModel>());

        if (bandEntity.getComponents() != null) {
            bandEntity.getComponents().stream()
                    .sorted((c1, c2) -> {
                        return c1.getUser().getFirstName().compareTo(c2.getUser().getFirstName());
                    }).filter(c -> c.isConfirmed()) // We show only confirmed components
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

                                if (c.getPlayedInstruments() != null) {
                                    c.getPlayedInstruments().stream().forEach(i -> {
                                        ComponentModel.Instrument instrument = componentModel.createInstrumentInstance();
                                        instrument.setName(i.getName());
                                        instrument.setId(i.getId().toString());
                                        if (c.getPlayedInstruments() != null && c.getPlayedInstruments().stream().filter(pi -> pi.getId().equals(i.getId())).findFirst().isPresent()) {
                                            instrument.setSelected(true);
                                        }

                                        componentModel.getInstruments().add(instrument);
                                    });
                                }

                                // Adds instruments from the user set
                                /*if (c.getUser().getPlayedMusicInstruments() != null) {
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
                                });*/


                                bandModel.getComponents().add(componentModel);
                            });
        }
    }

    private VideosModel getVideosModel(Long bandId, int currentPage) {
        VideosModel videosModel = new VideosModel();

        VideosCriteria videosCriteria = new VideosCriteria();

        videosCriteria.setBandId(bandId);
        videosCriteria.setCurrentPage(currentPage);
        videosCriteria.setPageSize(this.videosPageSize);

        PagedEntity<VideoEntity> pagedEntity = this.bandDao.getBandVideos(videosCriteria);

        videosModel.setCurrentPage(pagedEntity.getCurrentPage());
        videosModel.setNextRows(pagedEntity.getNextPageRows());
        videosModel.setPreviousRows(pagedEntity.getPreviousPageRows());
        videosModel.setVideos(pagedEntity.getResults().stream().sorted((v1, v2) -> v2.getCreated().compareTo(v1.getCreated())).collect(Collectors.toList()));

        return videosModel;
    }

    private GalleryModel getGalleryModel(Long bandId, int currentPage) {
        GalleryModel galleryModel = new GalleryModel();

        GalleryCriteria galleryCriteria = new GalleryCriteria();

        galleryCriteria.setBandId(bandId);
        galleryCriteria.setCurrentPage(currentPage);
        galleryCriteria.setPageSize(this.galleryPageSize);

        PagedEntity<ImageEntity> pagedEntity = this.bandDao.getBandGallery(galleryCriteria);

        galleryModel.setCurrentPage(pagedEntity.getCurrentPage());
        galleryModel.setNextRows(pagedEntity.getNextPageRows());
        galleryModel.setPreviousRows(pagedEntity.getPreviousPageRows());
        galleryModel.setImages(pagedEntity.getResults().stream().sorted((v1, v2) -> v2.getCreated().compareTo(v1.getCreated())).collect(Collectors.toList()));

        return galleryModel;
    }

    private AudioModel getAudioModel(Long bandId, int currentPage) {
        AudioModel audioModel = new AudioModel();

        AudioCriteria audioCriteria = new AudioCriteria();

        audioCriteria.setBandId(bandId);
        audioCriteria.setCurrentPage(currentPage);
        audioCriteria.setPageSize(this.audiosPageSize);

        PagedEntity<AudioEntity> pagedEntity = this.bandDao.getBandAudios(audioCriteria);

        audioModel.setCurrentPage(pagedEntity.getCurrentPage());
        audioModel.setNextRows(pagedEntity.getNextPageRows());
        audioModel.setPreviousRows(pagedEntity.getPreviousPageRows());
        audioModel.setAudios(pagedEntity.getResults().stream().sorted((a1, a2) -> a2.getCreated().compareTo(a1.getCreated())).collect(Collectors.toList()));

        return audioModel;
    }

    private Long getBandIdToView(Long bandId) {
        BandEntity bandEntity = this.bandUtilsDao.getByIdWithFetchedObjects(BandEntity.class, bandId, "status", "publishedVersion");

        String code = bandEntity.getStatus().getCode();

        if (!EntityStatus.PUBLISHED.toString().equalsIgnoreCase(code)) {

            // Checks if user is authenticated
            if (this.getAuthenticatedUser() == null) {
                bandId = null;
                this.addApplicationMessage(this.getMessage("viewband.cannot-view"), MessageSeverity.WARNING, null, null, this.getHomeUrl());
            } else {
                // Checks if current user is owner or adiminstrator
                if (!this.isOwnerOrAdmin(bandId)) {

                    // Checks if user is a band component
                    if (this.isUserBandComponent(bandId)) {
                        // gets the better id band to view
                        bandId = this.getBandIdByStatusEscalation(bandId);

                        if (bandId == null) {
                            this.addApplicationMessage(this.getMessage("viewband.cannot-view"), MessageSeverity.WARNING, null, null, this.getHomeUrl());
                        }

                    } else {
                        bandId = null;
                        this.addApplicationMessage(this.getMessage("viewband.cannot-view"), MessageSeverity.WARNING, null, null, this.getHomeUrl());
                    }
                }
            }
        }


        return bandId;
    }

    /**
     * Returns the correct band id based on the following priority:
     * 1. PUBLISHED (if exists)
     * 2. STAGE (if exists)
     * 3. NOT PUBLISHED
     * @param bandId
     * @return
     */
    private Long getBandIdByStatusEscalation(Long bandId) {
        Long id = null;

        String code = this.bandDao.getBandStatusCode(bandId);

        BandEntity bandEntity = null;

        if (EntityStatus.PUBLISHED.toString().equalsIgnoreCase(code)) {
            id = bandId;
        } else if (EntityStatus.STAGE.toString().equalsIgnoreCase(code)) {
            bandEntity = this.bandUtilsDao.getByIdWithFetchedObjects(BandEntity.class, bandId, "publishedVersion");

            String publishedVersionStatus = this.bandDao.getBandStatusCode(bandEntity.getId());

            // If published version status in PUBLISHED, we return its id otherwise we return stage version id
            if (EntityStatus.PUBLISHED.toString().equalsIgnoreCase(publishedVersionStatus)) {
                id = bandEntity.getId();
            } else {
                id = bandId;
            }
        } else if (EntityStatus.NOT_PUBLISHED.toString().equalsIgnoreCase(code)) {
            bandEntity = this.bandUtilsDao.getByIdWithFetchedObjects(BandEntity.class, bandId, "stageVersions");

            if (bandEntity.getStageVersions() != null && bandEntity.getStageVersions().size() > 0) {
                id = bandEntity.getStageVersions().get(0).getId();
            } else {
                id = bandId;
            }
        }
        return id;
    }

    private boolean isUserBandComponent(Long bandId) {
        boolean isComponent = false;

        if (this.getAuthenticatedUser() != null) {
            isComponent = this.bandDao.isBandComponent(bandId, this.getAuthenticatedUser().getId());
        }

        return isComponent;
    }

    private boolean isOwnerOrAdmin(Long bandId) {
        boolean isOwnerOrAdmin = false;

        BandEntity bandEntity = this.bandUtilsDao.getByIdWithFetchedObjects(BandEntity.class, bandId, "owners");

        Collection<BandOwnershipEntity> owners = bandEntity.getOwners();

        PortalUserModel currentUser = this.getAuthenticatedUser();

        if (currentUser != null) {
            Collection<String> allowedOwers = Arrays.asList(OwnerType.ADMINISTRATOR.toString(), OwnerType.OWNER.toString());

            // Checks if current user is one of allowed band's owners for editing
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
