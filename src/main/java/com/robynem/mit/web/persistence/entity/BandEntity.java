package com.robynem.mit.web.persistence.entity;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by robyn_000 on 09/01/2016.
 */
@Entity
@Table(name = "mit_band")
public class BandEntity extends BaseEntity {

    @Column
    protected String name;

    @Column
    protected String placeId;

    @Column
    protected String town;

    @Column
    protected String webSite;

    @Column(columnDefinition = "LONGTEXT")
    protected String biography;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdByUserId")
    protected UserEntity createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifiedByUserId")
    protected UserEntity modifiedBy;

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL)
    protected Set<BandOwnershipEntity> owners;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "bandLogo")
    protected ImageEntity bandLogo;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "mit_bandImage",
            joinColumns = {@JoinColumn(name = "bandId", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "imageId", nullable = false) }
    )
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    protected Set<ImageEntity> images;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "mit_bandVideo",
            joinColumns = {@JoinColumn(name = "bandId", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "videoId", nullable = false) }
    )
    protected Set<VideoEntity> videos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusId")
    protected EntityStatusEntity status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publishedBandId")
    /**
     * In the band staging mode, (when user hasn't published yet modifications),
     * we create a record on the band table, containig latest modifications, linked to its published version
     * with a self reference.
     */
    protected BandEntity publishedVersion;

    @OneToMany(mappedBy = "publishedVersion", fetch = FetchType.LAZY)
    protected Set<BandEntity> stageVersions;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "mit_bandMusicGenre",
            joinColumns = {@JoinColumn(name = "bandId", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "musicGenreId", nullable = false)}
    )
    protected Set<MusicGenreEntity> musicGenres;

    @OneToMany(mappedBy = "band", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, orphanRemoval = true)
    protected Set<BandComponentEntity> components;

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    protected Set<BandContactEntity> contacts;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "mit_bandAudio",
            joinColumns = {@JoinColumn(name = "bandId", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "audioId", nullable = false) }
    )
    protected Set<AudioEntity> audios;

    /*METHODS*/

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

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
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

    public Set<BandOwnershipEntity> getOwners() {
        return owners;
    }

    public void setOwners(Set<BandOwnershipEntity> owners) {
        this.owners = owners;
    }

    public Set<ImageEntity> getImages() {
        return images;
    }

    public void setImages(Set<ImageEntity> images) {
        this.images = images;
    }

    public Set<VideoEntity> getVideos() {
        return videos;
    }

    public void setVideos(Set<VideoEntity> videos) {
        this.videos = videos;
    }

    public EntityStatusEntity getStatus() {
        return status;
    }

    public void setStatus(EntityStatusEntity status) {
        this.status = status;
    }

    public BandEntity getPublishedVersion() {
        return publishedVersion;
    }

    public void setPublishedVersion(BandEntity publishedVersion) {
        this.publishedVersion = publishedVersion;
    }

    public Set<BandEntity> getStageVersions() {
        return stageVersions;
    }

    public void setStageVersions(Set<BandEntity> stageVersions) {
        this.stageVersions = stageVersions;
    }

    public ImageEntity getBandLogo() {
        return bandLogo;
    }

    public void setBandLogo(ImageEntity bandLogo) {
        this.bandLogo = bandLogo;
    }

    public Set<MusicGenreEntity> getMusicGenres() {
        return musicGenres;
    }

    public void setMusicGenres(Set<MusicGenreEntity> musicGenres) {
        this.musicGenres = musicGenres;
    }

    public Set<BandComponentEntity> getComponents() {
        return components;
    }

    public void setComponents(Set<BandComponentEntity> components) {
        this.components = components;
    }

    public Set<BandContactEntity> getContacts() {
        return contacts;
    }

    public void setContacts(Set<BandContactEntity> contacts) {
        this.contacts = contacts;
    }

    public Set<AudioEntity> getAudios() {
        return audios;
    }

    public void setAudios(Set<AudioEntity> audios) {
        this.audios = audios;
    }
}
