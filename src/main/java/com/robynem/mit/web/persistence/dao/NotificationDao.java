package com.robynem.mit.web.persistence.dao;

/**
 * Created by robyn_000 on 06/03/2016.
 */
public interface NotificationDao {

    void sendBandInvitation(Long senderUserId, Long receiverUserId, Long bandId);

    void sendBandComponentRemoval(Long senderUserId, Long receiverUserId, Long bandId);

}
