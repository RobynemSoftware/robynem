package com.robynem.mit.web.persistence.criteria;

/**
 * Created by robyn_000 on 03/04/2016.
 */
public class AudioCriteria extends BaseCriteria {

    private Long bandId;

    public Long getBandId() {
        return bandId;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
    }
}
