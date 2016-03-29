package com.robynem.mit.web.model.home;

import com.robynem.mit.web.util.ArtistType;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by roberto on 08/12/2015.
 */
public class NewArtistModel implements Serializable {

    protected Long id;

    protected String artistName;

    protected String artistDescription;

    protected String imgUrl;

    protected String artistType;

    protected Date firstPublishDate;


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

    public String getArtistType() {
        return artistType;
    }

    public void setArtistType(String artistType) {
        this.artistType = artistType;
    }

    public Date getFirstPublishDate() {
        return firstPublishDate;
    }

    public void setFirstPublishDate(Date firstPublishDate) {
        this.firstPublishDate = firstPublishDate;
    }
}
