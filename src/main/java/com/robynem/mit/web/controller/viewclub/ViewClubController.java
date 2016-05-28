package com.robynem.mit.web.controller.viewclub;

import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.model.editclub.ClubModel;
import com.robynem.mit.web.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by robyn_000 on 28/05/2016.
 */
@Controller
@RequestMapping("/viewClub")
public class ViewClubController extends BaseController {

    static final Logger LOG = LoggerFactory.getLogger(ViewClubController.class);

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
    public ModelAndView viewContent(@RequestParam Long bandId, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("viewband/viewClubContent");

        try {
            ClubModel clubModel = new ClubModel();

            /*bandId = this.getBandIdToView(bandId);

            if (bandId != null) {
                this.addSessionAttribute(Constants.VIEW_BAND_ID, bandId);

                this.populateGeneralInfoModel(bandId, clubModel);

                this.populateComponentsModel(bandId, clubModel);
            }*/

            modelMap.addAttribute("clubModel", clubModel);

        } catch (Exception e) {
            this.manageException(e, LOG, modelMap);
        }

        return modelAndView;
    }
}
