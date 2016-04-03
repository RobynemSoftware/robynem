package com.robynem.mit.web.model.viewband;

import com.robynem.mit.web.persistence.entity.AudioEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by robyn_000 on 03/04/2016.
 */
public class AudioModel implements Serializable {

    private List<AudioEntity> audios;

    private int previousRows;

    private int nextRows;

    private int currentPage;

    public List<AudioEntity> getAudios() {
        return audios;
    }

    public void setAudios(List<AudioEntity> audios) {
        this.audios = audios;
    }

    public int getPreviousRows() {
        return previousRows;
    }

    public void setPreviousRows(int previousRows) {
        this.previousRows = previousRows;
    }

    public int getNextRows() {
        return nextRows;
    }

    public void setNextRows(int nextRows) {
        this.nextRows = nextRows;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
