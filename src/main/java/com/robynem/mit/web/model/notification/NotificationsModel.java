package com.robynem.mit.web.model.notification;

import com.robynem.mit.web.persistence.entity.NotificationEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by robyn_000 on 07/03/2016.
 */
public class NotificationsModel implements Serializable {

    private List<NotificationEntity> notifications;

    private long unreadNotificationsCount;

    private int previousRows;

    private int nextRows;

    private int currentPage;




    public List<NotificationEntity> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationEntity> notifications) {
        this.notifications = notifications;
    }

    public long getUnreadNotificationsCount() {
        return unreadNotificationsCount;
    }

    public void setUnreadNotificationsCount(long unreadNotificationsCount) {
        this.unreadNotificationsCount = unreadNotificationsCount;
    }

    public int getPreviousRows() {
        return previousRows;
    }

    public void setPreviousRows(int previousRows) {
        this.previousRows = previousRows;
    }

    public int getNextRows() {
        return nextRows;
    }

    public void setNextRows(int nextRows) {
        this.nextRows = nextRows;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
