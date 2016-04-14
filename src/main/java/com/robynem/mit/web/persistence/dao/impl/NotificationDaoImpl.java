package com.robynem.mit.web.persistence.dao.impl;

import com.robynem.mit.web.persistence.dao.BaseDao;
import com.robynem.mit.web.persistence.dao.NotificationDao;
import com.robynem.mit.web.persistence.entity.*;
import com.robynem.mit.web.util.NotificationType;
import com.robynem.mit.web.util.SmtpFactory;
import com.robynem.mit.web.util.SmtpHelper;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by robyn_000 on 06/03/2016.
 */
@Service
public class NotificationDaoImpl extends BaseDao implements NotificationDao  {

    private static Logger LOG = LoggerFactory.getLogger(NotificationDaoImpl.class);

    @Autowired
    private SmtpFactory smtpFactory;

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void sendBandInvitation(Long senderUserId, Long receiverUserId, Long bandId) {

        if (senderUserId.equals(receiverUserId)) {
            return;
        }

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

        SmtpHelper smtpHelper = this.smtpFactory.getBandComponentInvitation(bandId, senderUserId, receiverUserId);

        if (smtpHelper != null) {
            smtpHelper.send();
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void sendExternalBandInvitation(Long senderUserId, String receiverEmailAddress, Long bandId) {

        this.hibernateTemplate.execute(session -> {

            boolean invitationSent = false;

            UserEntity checkUser = null;

            Query checkUserQuery = session.getNamedQuery("@HQL_GET_USER_BY_EMAIL_ADDRESS");
            checkUserQuery.setParameter("emailAddress", StringUtils.trimToEmpty(receiverEmailAddress));

            checkUser = (UserEntity) checkUserQuery.uniqueResult();

            if (checkUser != null) {
                LOG.debug("Email address {} already belongs to an existing user!", StringUtils.trimToEmpty(receiverEmailAddress));
                return false;
            }

            // Retrieves the External Band Notification Type
            Query invitationTypeQuery = session.getNamedQuery("@HQL_GET_NOTIFICATION_TYPE_BY_CODE");
            invitationTypeQuery.setParameter("code", NotificationType.BAND_EXTERNAL_INVITATION.toString());

            NotificationTypeEntity notificationType = (NotificationTypeEntity) invitationTypeQuery.uniqueResult();

            // Retrieves band entity
            BandEntity bandEntity = (BandEntity) session.get(BandEntity.class, bandId);

            // If it's a stage version, retrieves the published one
            if (bandEntity.getPublishedVersion() != null) {
                bandEntity = bandEntity.getPublishedVersion();
            }


            // Creates and stores new notification
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.setCreated(Calendar.getInstance().getTime());
            notificationEntity.setSenderUser((UserEntity) session.get(UserEntity.class, senderUserId));
            notificationEntity.setReceiverEmailAddress(StringUtils.trimToEmpty(receiverEmailAddress));
            notificationEntity.setBand(bandEntity);
            notificationEntity.setType(notificationType);

            session.save(notificationEntity);

            session.flush();

            this.smtpFactory.getExternalBandComponentInvitation(bandId, senderUserId, receiverEmailAddress).send();

            invitationSent = true;

           return invitationSent;
        });
    }

    @Override
    public boolean externalBandInvitationExists(Long senderUserId, String receiverEmailAddress, Long bandId) {
        return ((Long) this.hibernateTemplate.findByNamedQueryAndNamedParam("@HQL_GET_COUNT_EXTERNAL_BAND_INVITATION",
                new String[] {"emailAddress", "senderUserId", "bandId"},
                new Object[] {StringUtils.trimToEmpty(receiverEmailAddress), senderUserId, bandId}).get(0)) > 0;
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
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void sendBandInvitationAnswer(Long senderUserId, List<Long> receiverUserIds, Long bandId, boolean accept) {
        this.hibernateTemplate.execute(session -> {

            NotificationEntity notificationEntity = null;

            // Retrieves the Band Notification Type
            NotificationTypeEntity notificationTypeEntity = (NotificationTypeEntity) session.getNamedQuery("@HQL_GET_NOTIFICATION_TYPE_BY_CODE").
                    setParameter("code", accept ? NotificationType.BAND_INVITATION_ACCEPTED.toString() : NotificationType.BAND_INVITATION_DECLINED.toString()).
                    uniqueResult();

            // Retrieves band entity
            BandEntity bandEntity = (BandEntity) session.get(BandEntity.class, bandId);

            // If it's a stage version, retrieves the published one
            if (bandEntity.getPublishedVersion() != null) {
                bandEntity = bandEntity.getPublishedVersion();
            }

            // For each receiver it creates and stores new notification
            for (Long receiverUserId : receiverUserIds) {
                notificationEntity = new NotificationEntity();
                notificationEntity.setCreated(Calendar.getInstance().getTime());
                notificationEntity.setSenderUser((UserEntity) session.get(UserEntity.class, senderUserId));
                notificationEntity.setReceiverUser((UserEntity) session.get(UserEntity.class, receiverUserId));
                notificationEntity.setBand(bandEntity);
                notificationEntity.setType(notificationTypeEntity);

                session.save(notificationEntity);

                session.flush();

                this.smtpFactory.getBandComponentInvitationAnswer(bandId, senderUserId, receiverUserId, accept).send();

            }


            return null;
        });
    }

    @Override
    public List<NotificationEntity> getUnreadNotifications(Long receiverUserId) {
        return this.hibernateTemplate.findByNamedQueryAndNamedParam("@HQL_GET_UNREAD_NOTIFICATIONS", "receiverUserId", receiverUserId);
    }

    @Override
    public long getUnreadNotificationsCount(Long receiverUserId) {
        long count = 0;

        List<Object[]> result = this.hibernateTemplate.findByNamedQueryAndNamedParam("@HQL_GET_COUNT_UNREAD_NOTIFICATIONS", "receiverUserId", receiverUserId);

        if (result != null && result.size() > 0) {
            count = Long.valueOf(String.valueOf(result.get(0)));
        }

        return count;
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

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public void setNotificationRead(Long notificationId, Long receiverUserId) {
        this.hibernateTemplate.execute(session -> {

            Query query = session.getNamedQuery("@HQL_SET_NOTIFICATION_READ");
            query.setParameter("readNate", Calendar.getInstance().getTime());
            query.setParameter("id", notificationId);
            query.setParameter("receiverUserId", receiverUserId);

            query.executeUpdate();

            return null;
        });
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void reverseNotifications(Long receiverUserId) {

        this.hibernateTemplate.execute(session -> {
            boolean reverseComplete = false;

            UserEntity userEntity = (UserEntity) session.get(UserEntity.class, receiverUserId);

            if (userEntity != null) {

                // Retrieves notifications to reverse
                Query toReverseQuery = session.getNamedQuery("@HQL_GET_NOTIFICATIONS_TO_REVERSE");
                toReverseQuery.setParameter("emailAddress", userEntity.getEmailAddress());

                List<NotificationEntity> notifications = toReverseQuery.list();

                notifications.stream().forEach(n -> {
                    n.setReceiverEmailAddress(null);
                    n.setReceiverUser(userEntity);
                    session.update(n);
                });

                session.flush();

                this.doAutomaticActionsOnNotifications(notifications);

                reverseComplete = true;
            }

            return reverseComplete;
        });
    }

    @Override
    @Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    /**
     * Based on notification type, exec automations, such as, adding component to a band for BAND_EXTERNAL_INVITATION.
     */
    public void doAutomaticActionsOnNotifications(List<NotificationEntity> notifications) {
        //BAND_EXTERNAL_INVITATION
        /*Automaticaly adds as band component those notifications' receiver's user.*/

        List<NotificationEntity> extBandInvNotifications = notifications.stream().
                filter(n -> NotificationType.BAND_EXTERNAL_INVITATION.toString().equalsIgnoreCase(n.getType().getCode())).
                collect(Collectors.toList());

        this.hibernateTemplate.execute(session -> {

            extBandInvNotifications.stream().forEach(n -> {
                // Retrieves persisted notification with user data, instead of email address.
                NotificationEntity storedNotification = n;

                // Saves component on published version
                BandEntity bandEntity = storedNotification.getBand();

                BandComponentEntity bandComponentEntity = new BandComponentEntity();
                bandComponentEntity.setBand(bandEntity);
                bandComponentEntity.setUser(storedNotification.getReceiverUser());
                bandComponentEntity.setCreated(Calendar.getInstance().getTime());

                session.save(bandComponentEntity);

                // If we have a stage version, we add component to this version too
                if (bandEntity.getStageVersions() != null && bandEntity.getStageVersions().size() > 0) {
                    bandEntity = bandEntity.getStageVersions().get(0);

                    bandComponentEntity = new BandComponentEntity();
                    bandComponentEntity.setBand(bandEntity);
                    bandComponentEntity.setUser(storedNotification.getReceiverUser());
                    bandComponentEntity.setCreated(Calendar.getInstance().getTime());

                    session.save(bandComponentEntity);
                }

                session.flush();

            });

            return null;

        });

    }
}
