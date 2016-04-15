package com.robynem.mit.web.persistence.util;

import java.util.List;
import java.util.Map;

/**
 * Created by robyn_000 on 15/04/2016.
 */
public class BandMapResult extends MapResult {

    public static final String ID = "id";

    public static final String NAME = "bandName";

    public static final String LOGO_ID = "logoId";

    public BandMapResult() {
        super();
    }

    public BandMapResult(List<Map<String, Object>> results) {
        super(results);
    }
}
