package com.robynem.mit.web.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by robyn_000 on 09/01/2016.
 */
@Entity
@Table(name = "mit_video")
public class VideoEntity extends BaseEntity {

    @Column
    protected String youtubeUrl;

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }
}
