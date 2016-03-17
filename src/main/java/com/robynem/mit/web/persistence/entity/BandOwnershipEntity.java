package com.robynem.mit.web.persistence.entity;

import javax.persistence.*;

/**
 * Created by robyn_000 on 09/01/2016.
 */
@Entity
@Table(name = "mit_bandOwnership")
public class BandOwnershipEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    protected UserEntity user;

    @ManyToOne
    @JoinColumn(name = "bandId", nullable = false)
    protected BandEntity band;

    @ManyToOne
    @JoinColumn(name = "ownerTypeId")
    protected OwnerTypeEntity ownerType;

    public BandOwnershipEntity()  {
    }

    public BandOwnershipEntity(UserEntity user, OwnerTypeEntity ownerType) {
        this.user = user;
        this.ownerType = ownerType;
    }

    public BandOwnershipEntity(UserEntity user, BandEntity band, OwnerTypeEntity ownerType) {
        this.user = user;
        this.band = band;
        this.ownerType = ownerType;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public BandEntity getBand() {
        return band;
    }

    public void setBand(BandEntity band) {
        this.band = band;
    }

    public OwnerTypeEntity getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerTypeEntity ownerType) {
        this.ownerType = ownerType;
    }
}
