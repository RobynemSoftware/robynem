package com.robynem.mit.web.model.editband;

import java.io.Serializable;
import java.util.List;

/**
 * Created by robyn_000 on 24/01/2016.
 */
public class ComponentModel implements Serializable {

    protected Long id;

    protected Long userId;

    protected String profileImageId;

    protected String name;

    /**
     * Define if this component is a singer.
     */
    protected boolean singer;

    /**
     * Defines if this component is a dj
     */
    protected boolean discJockey;

    /**
     * Define if this component is this band's singer
     */
    protected boolean singerSelected;

    /**
     * Defines if this component is this band's dj
     */
    protected boolean discJockeySelected;

    protected List<Instrument> instruments;

    protected boolean confirmed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProfileImageId() {
        return profileImageId;
    }

    public void setProfileImageId(String profileImageId) {
        this.profileImageId = profileImageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(List<Instrument> instruments) {
        this.instruments = instruments;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Instrument createInstrumentInstance() {
        return new Instrument();
    }

    public boolean isSinger() {
        return singer;
    }

    public void setSinger(boolean singer) {
        this.singer = singer;
    }

    public boolean isDiscJockey() {
        return discJockey;
    }

    public void setDiscJockey(boolean discJockey) {
        this.discJockey = discJockey;
    }

    public boolean isSingerSelected() {
        return singerSelected;
    }

    public void setSingerSelected(boolean singerSelected) {
        this.singerSelected = singerSelected;
    }

    public boolean isDiscJockeySelected() {
        return discJockeySelected;
    }

    public void setDiscJockeySelected(boolean discJockeySelected) {
        this.discJockeySelected = discJockeySelected;
    }

    public class Instrument {
        protected String id;

        protected String name;

        protected boolean selected;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

}
