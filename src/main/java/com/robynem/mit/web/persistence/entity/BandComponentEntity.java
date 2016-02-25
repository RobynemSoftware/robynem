package com.robynem.mit.web.persistence.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by robyn_000 on 25/01/2016.
 */
@Entity
@Table(name = "mit_bandComponent")
public class BandComponentEntity extends BaseEntity {

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", nullable = false)
    protected UserEntity user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bandId", nullable = false)
    protected BandEntity band;

    /**
     * Define if this component is the band singer
     */
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    protected boolean singer;

    /**
     * Define if this component spins records
     */
    @Column(columnDefinition = "TINYINT DEFAULT 0")
    protected boolean discJockey;

    @Column(columnDefinition = "TINYINT DEFAULT 0")
    protected boolean confirmed;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "mit_bandComponentInstrument",
            joinColumns = {@JoinColumn(name = "bandComponentId", nullable = false) },
            inverseJoinColumns = { @JoinColumn(name = "musicalInstrumentId", nullable = false)}
    )
    protected Set<MusicalInstrumentEntity> playedInstruments;

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public BandEntity getBand() {
        return band;
    }

    public void setBand(BandEntity band) {
        this.band = band;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Set<MusicalInstrumentEntity> getPlayedInstruments() {
        return playedInstruments;
    }

    public void setPlayedInstruments(Set<MusicalInstrumentEntity> playedInstruments) {
        this.playedInstruments = playedInstruments;
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
}
