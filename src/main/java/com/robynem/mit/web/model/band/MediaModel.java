package com.robynem.mit.web.model.band;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robyn_000 on 13/02/2016.
 */
public class MediaModel implements Serializable {

    protected List<String> youtubeUrl = new ArrayList<String>();

    protected List<Long> imageIds = new ArrayList<Long>();

    public List<String> getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(List<String> youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public List<Long> getImageIds() {
        return imageIds;
    }

    public void setImageIds(List<Long> imageIds) {
        this.imageIds = imageIds;
    }
}
