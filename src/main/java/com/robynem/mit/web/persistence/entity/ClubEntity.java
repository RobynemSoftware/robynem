package com.robynem.mit.web.persistence.entity;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by robyn_000 on 16/04/2016.
 */
@Entity
@Table(name = "mit_club")
public class ClubEntity extends BaseEntity {

    @Column
    private String name;

    @Column
    private String placeId;

    @Column
    private String address;

    @Column
    private String town;

    @Column
    private String webSite;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdByUserId")
    private UserEntity createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifiedByUserId")
    private UserEntity modifiedBy;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "clubLogo")
    private ImageEntity clubLogo;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "mit_clubImage",
            joinColumns = {@JoinColumn(name = "clubId", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "imageId", nullable = false) }
    )
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<ImageEntity> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusId")
    private EntityStatusEntity status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publishedClubId")
    /**
     * In the club staging mode, (when user hasn't published yet modifications),
     * we create a record on the band table, containig latest modifications, linked to its published version
     * with a self reference.
     */
    private ClubEntity publishedVersion;

    @OneToMany(mappedBy = "publishedVersion", fetch = FetchType.LAZY)
    protected List<ClubEntity> stageVersions;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    protected List<ClubContactEntity> contacts;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "mit_club_clubGenre",
            joinColumns = {@JoinColumn(name = "clubId", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "clubGenreId", nullable = false)}
    )
    protected List<ClubGenreEntity> clubGenres;

    @Column
    private Date firstPublishDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "clubId", nullable = false)
    private List<ClubOpeningInfo> openingInfos;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClubOwnershipEntity> owners;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserEntity getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserEntity createdBy) {
        this.createdBy = createdBy;
    }

    public UserEntity getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(UserEntity modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public ImageEntity getClubLogo() {
        return clubLogo;
    }

    public void setClubLogo(ImageEntity clubLogo) {
        this.clubLogo = clubLogo;
    }

    public List<ImageEntity> getImages() {
        return images;
    }

    public void setImages(List<ImageEntity> images) {
        this.images = images;
    }

    public EntityStatusEntity getStatus() {
        return status;
    }

    public void setStatus(EntityStatusEntity status) {
        this.status = status;
    }

    public ClubEntity getPublishedVersion() {
        return publishedVersion;
    }

    public void setPublishedVersion(ClubEntity publishedVersion) {
        this.publishedVersion = publishedVersion;
    }

    public List<ClubEntity> getStageVersions() {
        return stageVersions;
    }

    public void setStageVersions(List<ClubEntity> stageVersions) {
        this.stageVersions = stageVersions;
    }

    public List<ClubContactEntity> getContacts() {
        return contacts;
    }

    public void setContacts(List<ClubContactEntity> contacts) {
        this.contacts = contacts;
    }

    public Date getFirstPublishDate() {
        return firstPublishDate;
    }

    public void setFirstPublishDate(Date firstPublishDate) {
        this.firstPublishDate = firstPublishDate;
    }

    public List<ClubGenreEntity> getClubGenres() {
        return clubGenres;
    }

    public void setClubGenres(List<ClubGenreEntity> clubGenres) {
        this.clubGenres = clubGenres;
    }

    public List<ClubOpeningInfo> getOpeningInfos() {
        return openingInfos;
    }

    public void setOpeningInfos(List<ClubOpeningInfo> openingInfos) {
        this.openingInfos = openingInfos;
    }

    public List<ClubOwnershipEntity> getOwners() {
        return owners;
    }

    public void setOwners(List<ClubOwnershipEntity> owners) {
        this.owners = owners;
    }
}