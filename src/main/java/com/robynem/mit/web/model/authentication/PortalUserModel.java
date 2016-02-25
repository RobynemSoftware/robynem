package com.robynem.mit.web.model.authentication;

import java.io.Serializable;

/**
 * Created by robyn_000 on 24/12/2015.
 */
public class PortalUserModel implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private Long facebookId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Long getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(Long facebookId) {
        this.facebookId = facebookId;
    }
}
