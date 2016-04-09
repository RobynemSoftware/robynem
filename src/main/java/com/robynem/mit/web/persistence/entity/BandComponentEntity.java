package com.robynem.mit.web.persistence.entity;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Set;

/**
 * Created by robyn_000 on 25/01/2016.
 */
@Entity
@Table(name = "mit_bandComponent")
@NamedQueries({
        @NamedQuery(name = "@HQL_GET_BAND_COMPONENT_BY_BID_AND_UID",
                query = "from BandComponentEntity bc " +
                        "inner join fetch bc.band b " +
                "where b.id = :bandId and bc.user.id = :userId")
})
public class BandComponentEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    protected UserEntity user;

    @ManyToOne
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

    @ManyToMany(fetch = FetchType.LAZY)
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
