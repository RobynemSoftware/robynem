package com.robynem.mit.web.persistence.entity;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;

/**
 * Created by robyn_000 on 30/01/2016.
 */
@Entity
@Table(name = "mit_clubContact")
@NamedQueries({
        @NamedQuery(name = "@HQL_DELETE_ALL_CLUB_CONTACTS", query = "delete from ClubContactEntity c where c.club.id = :clubId")
})
public class ClubContactEntity extends BaseEntity {

    @Column
    protected String emailAddress;

    @Column
    protected String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "clubId", nullable = false)
    protected ClubEntity club;

    public ClubContactEntity() {
    }

    public ClubContactEntity(String emailAddress, String phoneNumber) {
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public ClubContactEntity(String emailAddress, String phoneNumber, ClubEntity clubEntity) {
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.club = clubEntity;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ClubEntity getClub() {
        return club;
    }

    public void setClub(ClubEntity club) {
        this.club = club;
    }
}
