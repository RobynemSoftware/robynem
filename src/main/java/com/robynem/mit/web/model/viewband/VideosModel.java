package com.robynem.mit.web.model.viewband;

import com.robynem.mit.web.persistence.entity.VideoEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by robyn_000 on 01/04/2016.
 */
public class VideosModel implements Serializable {

    private List<VideoEntity> videos;

    private int previousRows;

    private int nextRows;

    private int currentPage;

    public List<VideoEntity> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoEntity> videos) {
        this.videos = videos;
    }

    public int getPreviousRows() {
        return previousRows;
    }

    public void setPreviousRows(int previousRows) {
        this.previousRows = previousRows;
    }

    public int getNextRows() {
        return nextRows;
    }

    public void setNextRows(int nextRows) {
        this.nextRows = nextRows;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
