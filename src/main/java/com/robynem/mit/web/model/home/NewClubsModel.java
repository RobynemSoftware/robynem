package com.robynem.mit.web.model.home;

import java.io.Serializable;

/**
 * Created by roberto on 09/12/2015.
 */
public class NewClubsModel implements Serializable {
    protected String clubName;

    protected String clubType;

    protected String imgUrl;

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getClubType() {
        return clubType;
    }

    public void setClubType(String clubType) {
        this.clubType = clubType;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
