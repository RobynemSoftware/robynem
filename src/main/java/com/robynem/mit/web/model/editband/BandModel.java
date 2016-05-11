package com.robynem.mit.web.model.editband;

import com.robynem.mit.web.model.ContactModel;
import com.robynem.mit.web.model.MediaModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robyn_000 on 15/01/2016.
 */
public class BandModel implements Serializable {

    protected Long id;

    protected Long logoImageId;

    protected String biography;

    protected String name;

    protected List<String> genres;

    protected String town;

    protected String placeId;

    protected String webSite;

    protected List<ComponentModel> components;

    protected List<ContactModel> emailContacts = new ArrayList<ContactModel>();

    protected List<ContactModel> phoneNumberContacts = new ArrayList<ContactModel>();

    protected MediaModel mediaModel = new MediaModel();

    protected boolean owner;

    protected boolean admin;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
