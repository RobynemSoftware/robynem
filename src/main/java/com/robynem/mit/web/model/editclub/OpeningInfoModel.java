package com.robynem.mit.web.model.editclub;

import java.io.Serializable;

/**
 * Created by robyn_000 on 01/05/2016.
 */
public class OpeningInfoModel implements Serializable {

    private Integer startDay;

    private Integer endDay;

    private Integer startHour;

    private Integer endHour;

    private boolean opened;

    public Integer getStartDay() {
        return startDay;
    }

    public void setStartDay(Integer startDay) {
        this.startDay = startDay;
    }

    public Integer getEndDay() {
        return endDay;
    }

    public void setEndDay(Integer endDay) {
        this.endDay = endDay;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }
}
