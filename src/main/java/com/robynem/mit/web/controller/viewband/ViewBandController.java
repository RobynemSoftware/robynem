package com.robynem.mit.web.controller.viewband;

import com.robynem.mit.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by robyn_000 on 26/03/2016.
 */
@Controller
@RequestMapping("/viewBand")
public class ViewBandController extends BaseController {

    static final Logger LOG = LoggerFactory.getLogger(ViewBandController.class);

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

        return modelAndView;
    }

}
