package com.robynem.mit.web.persistence.entity;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;

/**
 * Created by robyn_000 on 16/04/2016.
 */
@Entity
@Table(name = "mit_clubOpeningInfo")
public class ClubOpeningInfo extends BaseEntity {

    @Column
    private Integer startDay; // for DayOfWeek enum

    @Column
    private Integer endDay; // for DayOfWeek enum

    @Column
    @Temporal(TemporalType.TIME)
    private Date startHour;

    @Column
    @Temporal(TemporalType.TIME)
    private Date endHour;

    @Column(columnDefinition = "TINYINT DEFAULT 0")
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

    public Date getStartHour() {
        return startHour;
    }

    public void setStartHour(Date startHour) {
        this.startHour = startHour;
    }

    public Date getEndHour() {
        return endHour;
    }

    public void setEndHour(Date endHour) {
        this.endHour = endHour;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }
}
