package com.robynem.mit.web.persistence.util;

import java.util.List;
import java.util.Map;

/**
 * Created by robyn_000 on 26/03/2016.
 */
public class ArtistMapResult extends MapResult {

    public ArtistMapResult() {
        super();
    }

    public ArtistMapResult(List<Map<String, Object>> results) {
        super(results);
    }

    public static final String ID = "id";

    public static final String NAME = "artistName";

    public static final String ARTIST_DESCRIPTION = "artistDescription";

    public static final String IMAGE_ID = "imageId";

    public static final String ARTIST_TYPE = "artistType";
}
