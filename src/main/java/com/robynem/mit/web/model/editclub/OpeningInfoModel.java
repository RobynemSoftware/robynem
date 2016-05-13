package com.robynem.mit.web.model.editclub;

import java.io.Serializable;

/**
 * Created by robyn_000 on 01/05/2016.
 */
public class OpeningInfoModel implements Serializable {

    public static final String INDEX_KEY = "OI_index";
    public static final String START_DAY_KEY = "OI_startDay_";
    public static final String END_DAY_KEY = "OI_endDay_";
    public static final String START_HOUR_KEY = "OI_startHour_";
    public static final String END_HOUR_KEY = "OI_endHour_";
    public static final String OPENED_KEY = "OI_opened_";

    private Long id;

    private Integer startDay;

    private Integer endDay;

    private String startHour;

    private String endHour;

    private boolean opened;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }
}
