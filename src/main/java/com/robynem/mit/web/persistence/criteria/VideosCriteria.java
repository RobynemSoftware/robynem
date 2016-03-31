package com.robynem.mit.web.persistence.criteria;

/**
 * Created by rrennaloiacono on 31/03/2016.
 */
public class VideosCriteria extends BaseCriteria {

    private Long bandId;

    public Long getBandId() {
        return bandId;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
    }
}
