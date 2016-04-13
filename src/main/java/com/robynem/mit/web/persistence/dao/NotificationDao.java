package com.robynem.mit.web.persistence.dao;

import com.robynem.mit.web.persistence.entity.NotificationEntity;
import com.robynem.mit.web.persistence.entity.PagedEntity;
import com.robynem.mit.web.util.NotificationType;
import com.robynem.mit.web.util.SmtpHelper;

import java.util.List;

/**
 * Created by robyn_000 on 06/03/2016.
 */
public interface NotificationDao {

    void sendBandInvitation(Long senderUserId, Long receiverUserId, Long bandId);

    void sendExternalBandInvitation(Long senderUserId, String receiverEmailAddress, Long bandId);

    void sendBandComponentRemoval(Long senderUserId, Long receiverUserId, Long bandId);

    void sendBandInvitationAnswer(Long senderUserId, List<Long> receiverUserIds, Long bandId, boolean accept);

    boolean externalBandInvitationExists(Long senderUserId, String receiverEmailAddress, Long bandId);

    List<NotificationEntity> getUnreadNotifications(Long receiverUserId);

    long getUnreadNotificationsCount(Long receiverUserId);

    PagedEntity<NotificationEntity> getNotifications(Long receiverUserId, Integer pageSize, Integer currentPage);

    void setNotificationRead(Long notificationId, Long receiverUserId);

    void reverseNotifications(Long receiverUserId);

}
