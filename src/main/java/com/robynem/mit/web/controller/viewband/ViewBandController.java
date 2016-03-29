package com.robynem.mit.web.controller.viewband;

import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.model.band.BandModel;
import com.robynem.mit.web.model.band.ContactModel;
import com.robynem.mit.web.persistence.dao.BandDao;
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

}
