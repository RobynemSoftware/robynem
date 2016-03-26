package com.robynem.mit.web.model.home;

import com.robynem.mit.web.util.ArtistType;

import java.io.Serializable;

/**
 * Created by roberto on 08/12/2015.
 */
public class NewArtistModel implements Serializable {

    protected Long id;

    protected String artistName;

    protected String artistDescription;

    protected String imgUrl;

    protected ArtistType artistType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public ArtistType getArtistType() {
        return artistType;
    }

    public void setArtistType(ArtistType artistType) {
        this.artistType = artistType;
    }
}
