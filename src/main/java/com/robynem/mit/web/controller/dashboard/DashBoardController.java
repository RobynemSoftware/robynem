package com.robynem.mit.web.controller.dashboard;

import com.robynem.mit.web.controller.BaseController;
import com.robynem.mit.web.model.notification.NotificationsModel;
import com.robynem.mit.web.persistence.dao.BandDao;
import com.robynem.mit.web.persistence.dao.NotificationDao;
import com.robynem.mit.web.persistence.entity.BandEntity;
import com.robynem.mit.web.persistence.entity.ImageEntity;
import com.robynem.mit.web.persistence.entity.NotificationEntity;
import com.robynem.mit.web.persistence.entity.PagedEntity;
import com.robynem.mit.web.persistence.util.BandMapResult;
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
import org.springframework.web.servlet.view.AbstractView;

import java.util.ArrayList;
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

    @RequestMapping("/viewPlayingBands")
    public ModelAndView viewPlayingBands(ModelMap modelMap) {
        ModelAndView modelAndView = new ModelAndView("dashboard/dashboardPlayingBands");

        try {

            // Retrieves owned band
            modelMap.addAttribute("playingBands", this.getPlayingBans());

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

    @RequestMapping("/setNotificationRead")
    public AbstractView setNotificationRead(@RequestParam Long notificationId, ModelMap modelMap) {
        try {
            this.notificationDao.setNotificationRead(notificationId, this.getAuthenticatedUser().getId());
            modelMap.addAttribute("success", true);
        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        }

        return this.getJsonView(modelMap);
    }

    @RequestMapping("/getUnreadNotificationsCount")
    public AbstractView getUnreadNotificationsCount(ModelMap modelMap) {
        try {
            if (this.getAuthenticatedUser() != null) {
                long count = this.notificationDao.getUnreadNotificationsCount(this.getAuthenticatedUser().getId());

                LOG.debug("Unread notification coint: {}", count);

                if (count > 0) {
                    modelMap.addAttribute("count", count);
                }
            }
        } catch (Throwable e) {
            modelMap.addAttribute("success", false);
            this.manageException(e, LOG, modelMap);
        }

        return this.getJsonView(modelMap);
    }

    private List<BandEntity> getOwnedBans() {
        return this.bandDao.getOwnedBands(this.getAuthenticatedUser().getId(), OwnerType.OWNER);
    }

    private List<BandEntity> getPlayingBans() {
        List<BandEntity> list = new ArrayList<>();

        BandMapResult bandMapResult = this.bandDao.getPlayingBands(this.getAuthenticatedUser().getId());

        BandEntity bandEntity = null;

        while (bandMapResult.next()) {
            bandEntity = new BandEntity();
            bandEntity.setId(bandMapResult.get(BandMapResult.ID));
            bandEntity.setName(bandMapResult.get(BandMapResult.NAME));

            if (bandMapResult.get(BandMapResult.LOGO_ID) != null) {
                bandEntity.setBandLogo(new ImageEntity());
                bandEntity.getBandLogo().setId(bandMapResult.get(BandMapResult.LOGO_ID));
            }

            list.add(bandEntity);
        }

        return list;
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
