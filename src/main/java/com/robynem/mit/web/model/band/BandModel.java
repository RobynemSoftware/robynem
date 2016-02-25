package com.robynem.mit.web.model.band;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robyn_000 on 15/01/2016.
 */
public class BandModel implements Serializable {

    private Long logoImageId;

    private String biography;

    private String name;

    private List<String> genres;

    protected String town;

    private String placeId;

    private String webSite;

    private List<ComponentModel> components;

    private List<ContactModel> emailContacts = new ArrayList<ContactModel>();

    private List<ContactModel> phoneNumberContacts = new ArrayList<ContactModel>();

    private MediaModel mediaModel = new MediaModel();



    public Long getLogoImageId() {
        return logoImageId;
    }

    public void setLogoImageId(Long logoImageId) {
        this.logoImageId = logoImageId;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public List<ComponentModel> getComponents() {
        return components;
    }

    public void setComponents(List<ComponentModel> components) {
        this.components = components;
    }

    public List<ContactModel> getEmailContacts() {
        return emailContacts;
    }

    public void setEmailContacts(List<ContactModel> emailContacts) {
        this.emailContacts = emailContacts;
    }

    public List<ContactModel> getPhoneNumberContacts() {
        return phoneNumberContacts;
    }

    public void setPhoneNumberContacts(List<ContactModel> phoneNumberContacts) {
        this.phoneNumberContacts = phoneNumberContacts;
    }

    public MediaModel getMediaModel() {
        return mediaModel;
    }

    public void setMediaModel(MediaModel mediaModel) {
        this.mediaModel = mediaModel;
    }
}
