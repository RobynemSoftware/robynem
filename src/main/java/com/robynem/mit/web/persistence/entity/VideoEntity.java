package com.robynem.mit.web.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;

/**
 * Created by robyn_000 on 09/01/2016.
 */
@Entity
@Table(name = "mit_video")
public class VideoEntity extends BaseEntity {

    @Column
    protected String youtubeUrl;

    public VideoEntity() {

    }

    public VideoEntity(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
        this.created = Calendar.getInstance().getTime();
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }
}
