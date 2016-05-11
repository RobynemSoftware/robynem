package com.robynem.mit.web.model.editband;

import java.io.Serializable;

/**
 * Created by robyn_000 on 24/01/2016.
 */
public class ComponentAutocompleteModel implements Serializable {

    private String userId;

    private String imageUrl;

    private String name;

    private String label;

    private String value;

    private String emailAddress;

    public ComponentAutocompleteModel() {
    }

    public ComponentAutocompleteModel(String userId, String imageUrl, String name) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.name = name;

        this.label = name;
        this.value = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
