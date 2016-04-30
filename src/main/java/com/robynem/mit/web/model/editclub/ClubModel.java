package com.robynem.mit.web.model.editclub;

import com.robynem.mit.web.model.editband.BandModel;

/**
 * Created by robyn_000 on 30/04/2016.
 */
public class ClubModel extends BandModel {

    protected String description;

    protected String address;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
