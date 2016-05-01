package com.robynem.mit.web.model.editclub;

import com.robynem.mit.web.model.editband.BandModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robyn_000 on 30/04/2016.
 */
public class ClubModel extends BandModel {

    protected String description;

    protected String address;

    protected List<OpeningInfoModel> openingInfos = new ArrayList<>();

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

    public List<OpeningInfoModel> getOpeningInfos() {
        return openingInfos;
    }

    public void setOpeningInfos(List<OpeningInfoModel> openingInfos) {
        this.openingInfos = openingInfos;
    }
}
