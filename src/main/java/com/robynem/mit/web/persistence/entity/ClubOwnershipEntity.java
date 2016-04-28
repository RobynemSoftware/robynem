package com.robynem.mit.web.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by robyn_000 on 18/04/2016.
 */
@Entity
@Table(name = "mit_clubOwnership")
public class ClubOwnershipEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    protected UserEntity user;

    @ManyToOne
    @JoinColumn(name = "clubId", nullable = false)
    protected ClubEntity club;

    @ManyToOne
    @JoinColumn(name = "ownerTypeId")
    protected OwnerTypeEntity ownerType;

    public ClubOwnershipEntity()  {
    }

    public ClubOwnershipEntity(UserEntity user, OwnerTypeEntity ownerType) {
        this.user = user;
        this.ownerType = ownerType;
    }

    public ClubOwnershipEntity(UserEntity user, ClubEntity club, OwnerTypeEntity ownerType) {
        this.user = user;
        this.club = club;
        this.ownerType = ownerType;
    }


    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ClubEntity getClub() {
        return club;
    }

    public void setClub(ClubEntity club) {
        this.club = club;
    }

    public OwnerTypeEntity getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(OwnerTypeEntity ownerType) {
        this.ownerType = ownerType;
    }
}
