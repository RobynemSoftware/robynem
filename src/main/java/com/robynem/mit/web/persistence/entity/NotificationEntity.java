package com.robynem.mit.web.persistence.entity;

import com.robynem.mit.web.util.NotificationType;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by robyn_000 on 05/03/2016.
 */
@Entity
@Table(name = "mit_notification")
@NamedQueries({
        @NamedQuery(name = "@HQL_GET_UNREAD_NOTIFICATIONS",
                query = "from NotificationEntity n inner join fetch n.type t where n.readDate is null and n.receiverUser.id = :receiverUserId order by n.created desc "),
        @NamedQuery(name = "@HQL_GET_NOTIFICATIONS_BY_RECEIVER_AND_TYPE", query = "from NotificationEntity n " +
                    "inner join fetch n.type t " +
                "where t.code = :typeCode and n.receiverUser.id = :receiverUserId order by n.created desc"),
        @NamedQuery(name = "@HQL_GET_COUNT_NOTIFICATIONS_BY_RECEIVER_AND_TYPE", query = " select count(n.id) from NotificationEntity n " +
                    "inner join n.type t " +
                "where n.type.code = :typeCode and n.receiverUser.id = :receiverUserId"),
        @NamedQuery(name = "@HQL_GET_NOTIFICATIONS_BAND_INVITATION", query = "from NotificationEntity n " +
                    "inner join fetch n.type t " +
                    "inner join fetch n.band nb " +
                "where t.code = :typeCode and n.receiverUser.id = :receiverUserId order by n.created desc")
})
public class NotificationEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "receiverUserId")
    private UserEntity receiverUser;

    /**
     * When a notification must be sent to a non registered yet user this field will be populated
     */
    @Column
    private String receiverEmailAddress;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "senderUserId")
    private UserEntity senderUser;

    @Column
    private Date readDate;

    @Column(columnDefinition = "LONGTEXT")
    private String message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeId")
    private NotificationTypeEntity type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "mit_bandNotification",
            joinColumns = {@JoinColumn(name = "bandId", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "notificationId", nullable = false)}
    )
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private BandEntity band;


    public UserEntity getReceiverUser() {
        return receiverUser;
    }

    public void setReceiverUser(UserEntity receiverUser) {
        this.receiverUser = receiverUser;
    }

    public UserEntity getSenderUser() {
        return senderUser;
    }

    public void setSenderUser(UserEntity senderUser) {
        this.senderUser = senderUser;
    }

    public Date getReadDate() {
        return readDate;
    }

    public void setReadDate(Date readDate) {
        this.readDate = readDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationTypeEntity getType() {
        return type;
    }

    public void setType(NotificationTypeEntity type) {
        this.type = type;
    }

    public String getReceiverEmailAddress() {
        return receiverEmailAddress;
    }

    public void setReceiverEmailAddress(String receiverEmailAddress) {
        this.receiverEmailAddress = receiverEmailAddress;
    }

    public BandEntity getBand() {
        return band;
    }

    public void setBand(BandEntity band) {
        this.band = band;
    }
}
