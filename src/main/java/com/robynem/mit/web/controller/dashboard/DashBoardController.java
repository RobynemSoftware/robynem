package com.robynem.mit.web.controller.dashboard;

import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.model.notification.NotificationsModel;
import com.robynem.mit.web.persistence.dao.BandDao;
import com.robynem.mit.web.persistence.dao.NotificationDao;
import com.robynem.mit.web.persistence.entity.BandEntity;
import com.robynem.mit.web.persistence.entity.NotificationEntity;
import com.robynem.mit.web.persistence.entity.PagedEntity;
import com.robynem.mit.web.util.OwnerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by robyn_000 on 24/01/2016.
 */
@Controller
@RequestMapping("/private/dashboard")
public class DashBoardController extends BaseController {

    static final Logger LOG = LoggerFactory.getLogger(DashBoardController.class);

    @Value("${notifications.page-size}")
    private int notificationsPageSize;

    @Autowired
    private BandDao bandDao;

    @Autowired
    private NotificationDao notificationDao;

    @RequestMapping
    public ModelAndView view(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("dashboard/dashboard");

        return modelAndView;
    }

    @RequestMapping("/viewOwnedBands")
    public ModelAndView viewOwnedBands(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("dashboard/dashboardOwnedBands");

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

    @RequestMapping("/viewNotifications")
    public ModelAndView viewNotifications(@RequestParam(required = false, defaultValue = "1") int currentPage, ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("dashboard/dashboardNotifications");

        try {

            // Retrieves notifications
            modelMap.addAttribute("notificationModel", this.getNotificationsModel(currentPage));

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

    private NotificationsModel getNotificationsModel(Integer currentPage) {
        NotificationsModel notificationsModel = new NotificationsModel();

        PagedEntity<NotificationEntity> resultEntity = this.notificationDao.getNotifications(this.getAuthenticatedUser().getId(), this.notificationsPageSize, currentPage);

        notificationsModel.setNotifications(resultEntity.getResults());
        notificationsModel.setPreviousRows(resultEntity.getPreviousPageRows());
        notificationsModel.setNextRows(resultEntity.getNextPageRows());
        notificationsModel.setCurrentPage(resultEntity.getCurrentPage());
        notificationsModel.setUnreadNotificationsCount(resultEntity.getResults().stream().filter(n -> n.getReadDate() == null).count());

        return notificationsModel;
    }
}
