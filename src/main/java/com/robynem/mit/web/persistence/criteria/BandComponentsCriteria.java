package com.robynem.mit.web.persistence.criteria;

/**
 * Created by robyn_000 on 01/02/2016.
 */
public class BandComponentsCriteria {

    protected Long instrumentId;

    protected Long genreId;

    protected String keyword;

    protected String placeId;

    protected Long bandId;

    protected boolean singer;

    protected boolean dj;

    public Long getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(Long instrumentId) {
        this.instrumentId = instrumentId;
    }

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Long getBandId() {
        return bandId;
    }

    public void setBandId(Long bandId) {
        this.bandId = bandId;
    }

    public boolean isSinger() {
        return singer;
    }

    public void setSinger(boolean singer) {
        this.singer = singer;
    }

    public boolean isDj() {
        return dj;
    }

    public void setDj(boolean dj) {
        this.dj = dj;
    }
}
