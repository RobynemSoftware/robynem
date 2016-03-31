package com.robynem.mit.web.persistence.util;

import java.util.List;
import java.util.Map;

/**
 * Created by rrennaloiacono on 31/03/2016.
 */
public class VideoMapResult extends MapResult {

    public VideoMapResult() {
        super();
    }

    public VideoMapResult(List<Map<String, Object>> results) {
        super(results);
    }

    public static final String YOUTUBE_URL = "youtubeUrl";
}
