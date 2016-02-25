package com.robynem.mit.web.controller.dashboard;

import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.persistence.dao.BandDao;
import com.robynem.mit.web.persistence.entity.BandEntity;
import com.robynem.mit.web.util.OwnerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by robyn_000 on 24/01/2016.
 */
@Controller
@RequestMapping("/private/dashboard")
public class DashBoardController extends BaseController {

    static final Logger LOG = LoggerFactory.getLogger(DashBoardController.class);

    @Autowired
    private BandDao bandDao;

    @RequestMapping
    public ModelAndView view(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("dashboard/dashboard");

        try {

            // Retrieves owned band
            modelMap.addAttribute("ownedBands", this.getOwnedBans());

        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        } finally {
            // Force garbage collector to free memory.
            System.gc();
        }

        return modelAndView;
    }

    private List<BandEntity> getOwnedBans() {
        return this.bandDao.getOwnedBands(this.getAuthenticatedUser().getId(), OwnerType.OWNER);
    }
}
