package com.robynem.mit.web.persistence.dao.impl;

import com.robynem.mit.web.persistence.dao.BaseDao;
import com.robynem.mit.web.persistence.dao.NotificationDao;
import com.robynem.mit.web.persistence.entity.BandEntity;
import com.robynem.mit.web.persistence.entity.NotificationEntity;
import com.robynem.mit.web.persistence.entity.NotificationTypeEntity;
import com.robynem.mit.web.persistence.entity.UserEntity;
import com.robynem.mit.web.util.NotificationType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

/**
 * Created by robyn_000 on 06/03/2016.
 */
@Service
public class NotificationDaoImpl extends BaseDao implements NotificationDao  {

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void sendBandInvitation(Long senderUserId, Long receiverUserId, Long bandId) {

        NotificationEntity notificationEntity = null;

        // Retrieves the Band Notification Type
        NotificationTypeEntity bandInvitationNotificationType = (NotificationTypeEntity) this.hibernateTemplate.findByNamedQueryAndNamedParam("@HQL_GET_NOTIFICATION_TYPE_BY_CODE",
                                                                                                    "code", NotificationType.BAND_INVITATION.toString()).get(0);

        // Retrieves band entity
        BandEntity bandEntity = this.hibernateTemplate.get(BandEntity.class, bandId);

        // If it's a stage version, retrieves the published one
        if (bandEntity.getPublishedVersion() != null) {
            bandEntity = bandEntity.getPublishedVersion();
        }

        // Creates and stores new notification
        notificationEntity = new NotificationEntity();
        notificationEntity.setCreated(Calendar.getInstance().getTime());
        notificationEntity.setSenderUser(this.hibernateTemplate.get(UserEntity.class, senderUserId));
        notificationEntity.setReceiverUser(this.hibernateTemplate.get(UserEntity.class, receiverUserId));
        notificationEntity.setData(String.valueOf(bandEntity.getId()));
        notificationEntity.setType(bandInvitationNotificationType);

        this.hibernateTemplate.save(notificationEntity);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void sendBandComponentRemoval(Long senderUserId, Long receiverUserId, Long bandId) {

        this.hibernateTemplate.execute(session -> {
            NotificationEntity notificationEntity = null;

            // Retrieves the Band Notification Type

            NotificationTypeEntity bandComponentRemovalNotificationType = (NotificationTypeEntity) session.getNamedQuery("@HQL_GET_NOTIFICATION_TYPE_BY_CODE").
                    setParameter("code", NotificationType.BAND_COMPONENT_REMOVAL.toString()).
                    uniqueResult();

            // Retrieves band entity
            BandEntity bandEntity = (BandEntity) session.get(BandEntity.class, bandId);

            // If it's a stage version, retrieves the published one
            if (bandEntity.getPublishedVersion() != null) {
                bandEntity = bandEntity.getPublishedVersion();
            }

            // Creates and stores new notification
            notificationEntity = new NotificationEntity();
            notificationEntity.setCreated(Calendar.getInstance().getTime());
            notificationEntity.setSenderUser((UserEntity) session.get(UserEntity.class, senderUserId));
            notificationEntity.setReceiverUser((UserEntity) session.get(UserEntity.class, receiverUserId));
            notificationEntity.setData(String.valueOf(bandEntity.getId()));
            notificationEntity.setType(bandComponentRemovalNotificationType);

            session.save(notificationEntity);

            return null;
        });


    }
}
