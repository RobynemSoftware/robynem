package com.robynem.mit.web.model.home;

import java.io.Serializable;

/**
 * Created by roberto on 08/12/2015.
 */
public class NewArtistModel implements Serializable {
    protected String artistName;

    protected String artistDescription;

    protected String imgUrl;

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
