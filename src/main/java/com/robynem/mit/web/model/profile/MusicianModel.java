package com.robynem.mit.web.model.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robyn_000 on 07/01/2016.
 */
public class MusicianModel implements Serializable {

    private List<String> playedInstruments;

    private List<String> preferredGenres;

    private List<String> teachedInstruments;

    private boolean singer;

    private boolean engagementAvailable;

    private boolean musicMaster;

    private boolean musician;

    private boolean discJockey;

    private String biography;

    public MusicianModel() {
        this.playedInstruments = new ArrayList<String>();
        this.preferredGenres = new ArrayList<String>();
        this.teachedInstruments = new ArrayList<String>();
    }

    public List<String> getPlayedInstruments() {
        return playedInstruments;
    }

    public void setPlayedInstruments(List<String> playedInstruments) {
        this.playedInstruments = playedInstruments;
    }

    public List<String> getPreferredGenres() {
        return preferredGenres;
    }

    public void setPreferredGenres(List<String> preferredGenres) {
        this.preferredGenres = preferredGenres;
    }

    public List<String> getTeachedInstruments() {
        return teachedInstruments;
    }

    public void setTeachedInstruments(List<String> teachedInstruments) {
        this.teachedInstruments = teachedInstruments;
    }

    public boolean isSinger() {
        return singer;
    }

    public void setSinger(boolean singer) {
        this.singer = singer;
    }

    public boolean isEngagementAvailable() {
        return engagementAvailable;
    }

    public void setEngagementAvailable(boolean engagementAvailable) {
        this.engagementAvailable = engagementAvailable;
    }

    public boolean isMusicMaster() {
        return musicMaster;
    }

    public void setMusicMaster(boolean musicMaster) {
        this.musicMaster = musicMaster;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public boolean isMusician() {
        return musician;
    }

    public void setMusician(boolean musician) {
        this.musician = musician;
    }

    public boolean isDiscJockey() {
        return discJockey;
    }

    public void setDiscJockey(boolean discJockey) {
        this.discJockey = discJockey;
    }
}
