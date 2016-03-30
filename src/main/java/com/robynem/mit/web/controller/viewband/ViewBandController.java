package com.robynem.mit.web.controller.viewband;

import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.model.band.BandModel;
import com.robynem.mit.web.model.band.ComponentModel;
import com.robynem.mit.web.model.band.ContactModel;
import com.robynem.mit.web.persistence.dao.BandDao;
import com.robynem.mit.web.persistence.entity.AudioEntity;
import com.robynem.mit.web.persistence.entity.BandEntity;
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
import java.util.List;

/**
 * Created by robyn_000 on 26/03/2016.
 */
@Controller
@RequestMapping("/viewBand")
public class ViewBandController extends BaseController {

    static final Logger LOG = LoggerFactory.getLogger(ViewBandController.class);

    @Autowired
    private BandDao bandDao;

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

            this.populateGeneralInfoModel(bandId, bandModel);

            this.populateComponentsModel(bandId, bandModel);

            this.populateMediaModel(bandId, bandModel);

            modelMap.addAttribute("bandModel", bandModel);

        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        return modelAndView;
    }

    private void populateGeneralInfoModel(Long bandId, BandModel bandModel) {
        BandEntity bandEntity = this.bandDao.getBandGeneralInfo(bandId);

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

    private void populateMediaModel(Long bandId, BandModel bandModel) {
        BandEntity bandEntity = this.bandDao.getBandMedia(bandId);

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
                audioEntity.setSoundCloudUrl(a.getSoundCloudUrl());

                bandModel.getMediaModel().getAudios().add(audioEntity);
            });
        }
    }

}
