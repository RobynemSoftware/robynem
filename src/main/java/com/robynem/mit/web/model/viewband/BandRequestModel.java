package com.robynem.mit.web.model.viewband;

import java.io.Serializable;

/**
 * Created by robyn_000 on 09/04/2016.
 */
public class BandRequestModel implements Serializable {

    public static final String COMPONENT_INVITATION = "COMPONENT_INVITATION";

    private String type;

    public BandRequestModel() {
    }

    public BandRequestModel(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
