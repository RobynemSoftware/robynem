package com.robynem.mit.web.model;

import com.robynem.mit.web.persistence.entity.AudioEntity;
import com.robynem.mit.web.persistence.entity.VideoEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robyn_000 on 13/02/2016.
 */
public class MediaModel implements Serializable {

    protected List<VideoEntity> videos = new ArrayList<VideoEntity>();

    protected List<Long> imageIds = new ArrayList<Long>();

    protected List<AudioEntity> audios = new ArrayList<AudioEntity>();

    public List<Long> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<Long> imageIds) {
        this.imageIds = imageIds;
    }

    public List<AudioEntity> getAudios() {
        return audios;
    }

    public void setAudios(List<AudioEntity> audios) {
        this.audios = audios;
    }

    public List<VideoEntity> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoEntity> videos) {
        this.videos = videos;
    }
}
