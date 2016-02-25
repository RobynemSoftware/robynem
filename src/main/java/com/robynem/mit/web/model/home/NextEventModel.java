package com.robynem.mit.web.model.home;

import java.io.Serializable;

/**
 * Created by roberto on 08/12/2015.
 */
public class NextEventModel implements Serializable {

    protected String artistName;

    protected String artistDescription;

    protected String eventPlaceName;

    protected String eventPlaceType;

    protected String eventDetails;

    protected String imgUrl;

    protected boolean money;

    protected boolean phone;

    protected boolean specialOffer;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getArtistDescription() {
        return artistDescription;
    }

    public void setArtistDescription(String artistDescription) {
        this.artistDescription = artistDescription;
    }

    public String getEventPlaceName() {
        return eventPlaceName;
    }

    public void setEventPlaceName(String eventPlaceName) {
        this.eventPlaceName = eventPlaceName;
    }

    public String getEventPlaceType() {
        return eventPlaceType;
    }

    public void setEventPlaceType(String eventPlaceType) {
        this.eventPlaceType = eventPlaceType;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public boolean isMoney() {
        return money;
    }

    public void setMoney(boolean money) {
        this.money = money;
    }

    public boolean isPhone() {
        return phone;
    }

    public void setPhone(boolean phone) {
        this.phone = phone;
    }

    public boolean isSpecialOffer() {
        return specialOffer;
    }

    public void setSpecialOffer(boolean specialOffer) {
        this.specialOffer = specialOffer;
    }
}
