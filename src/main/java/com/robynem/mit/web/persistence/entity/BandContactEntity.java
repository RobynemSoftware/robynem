package com.robynem.mit.web.persistence.entity;

import javax.persistence.*;

/**
 * Created by robyn_000 on 30/01/2016.
 */
@Entity
@Table(name = "mit_bandContact")
@NamedQueries({
        @NamedQuery(name = "@HQL_DELETE_ALL_BAND_CONTACTS", query = "delete from BandContactEntity c where c.band.id = :bandId")
})
public class BandContactEntity extends BaseEntity {

    @Column
    protected String emailAddress;

    @Column
    protected String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "bandId", nullable = false)
    protected BandEntity band;

    public BandContactEntity() {
    }

    public BandContactEntity(String emailAddress, String phoneNumber) {
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public BandContactEntity(String emailAddress, String phoneNumber, BandEntity bandEntity) {
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.band = bandEntity;
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

    public BandEntity getBand() {
        return band;
    }

    public void setBand(BandEntity band) {
        this.band = band;
    }
}
