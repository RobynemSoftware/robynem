package com.robynem.mit.web.persistence.dao;

import com.robynem.mit.web.persistence.entity.NotificationEntity;
import com.robynem.mit.web.persistence.entity.PagedEntity;
import com.robynem.mit.web.util.NotificationType;

import java.util.List;

/**
 * Created by robyn_000 on 06/03/2016.
 */
public interface NotificationDao {

    void sendBandInvitation(Long senderUserId, Long receiverUserId, Long bandId);

    void sendBandComponentRemoval(Long senderUserId, Long receiverUserId, Long bandId);

    List<NotificationEntity> getUnreadNotifications(Long receiverUserId);

    PagedEntity<NotificationEntity> getNotifications(Long receiverUserId, Integer pageSize, Integer currentPage);

}
