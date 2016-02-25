package com.robynem.mit.web.model.profile;

import com.robynem.mit.web.util.UserGender;

import java.io.Serializable;

/**
 * Created by robyn_000 on 28/12/2015.
 */
public class ProfileModel implements Serializable {

    protected String firstName;

    protected String lastName;

    protected String gender;

    protected String emailAddress;

    protected String birthDate;

    protected Long birthDateMillis;

    protected String placeId;

    protected String town;

    protected Long profileImageId;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Long getBirthDateMillis() {
        return birthDateMillis;
    }

    public void setBirthDateMillis(Long birthDateMillis) {
        this.birthDateMillis = birthDateMillis;
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

    public Long getProfileImageId() {
        return profileImageId;
    }

    public void setProfileImageId(Long profileImageId) {
        this.profileImageId = profileImageId;
    }
}
