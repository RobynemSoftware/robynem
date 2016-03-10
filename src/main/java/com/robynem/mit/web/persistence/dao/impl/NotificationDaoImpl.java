package com.robynem.mit.web.persistence.dao.impl;

import com.robynem.mit.web.persistence.dao.BaseDao;
import com.robynem.mit.web.persistence.dao.NotificationDao;
import com.robynem.mit.web.persistence.entity.*;
import com.robynem.mit.web.util.NotificationType;
import org.hibernate.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        notificationEntity.setBand(bandEntity);
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
            notificationEntity.setBand(bandEntity);
            notificationEntity.setType(bandComponentRemovalNotificationType);

            session.save(notificationEntity);

            return null;
        });


    }

    @Override
    public List<NotificationEntity> getUnreadNotifications(Long receiverUserId) {
        return this.hibernateTemplate.findByNamedQueryAndNamedParam("@HQL_GET_UNREAD_NOTIFICATIONS", "receiverUserId", receiverUserId);
    }

    @Override
    public PagedEntity<NotificationEntity> getNotifications(Long receiverUserId, Integer pageSize, Integer currentPage) {
        return this.hibernateTemplate.execute(session -> {

            Map<String, Object> parameters = new HashMap<String, Object>() {
                {
                    put("receiverUserId", receiverUserId);
                }
            };

            PagedEntity<NotificationEntity> resultEntity = this.getPagingInfo("@HQL_GET_COUNT_NOTIFICATIONS",
                    parameters, pageSize, currentPage, session);



            Query query = session.getNamedQuery("@HQL_GET_NOTIFICATIONS");
            this.setParameters(query, parameters);
            //query.setParameter("receiverUserId", new Long(receiverUserId.longValue()));

            this.setPagination(query, resultEntity);

            resultEntity.setResults((List<NotificationEntity>)query.list());

            return resultEntity;

        });
    }
}
