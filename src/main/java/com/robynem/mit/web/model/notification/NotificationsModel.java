package com.robynem.mit.web.model.notification;

import com.robynem.mit.web.persistence.entity.NotificationEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by robyn_000 on 07/03/2016.
 */
public class NotificationsModel implements Serializable {

    private List<NotificationEntity> unreadNotifications;

    public List<NotificationEntity> getUnreadNotifications() {
        return unreadNotifications;
    }

    public void setUnreadNotifications(List<NotificationEntity> unreadNotifications) {
        this.unreadNotifications = unreadNotifications;
    }
}
