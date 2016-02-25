package com.robynem.mit.web.persistence.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by robyn_000 on 05/01/2016.
 */
@Entity
@Table(name = "mit_musicalInstrument")
@NamedQueries({
        @NamedQuery(name = "@HQL_GET_ALL_MUSICAL_INSTRUMNTS", query = "from MusicalInstrumentEntity m order by m.name asc ")
})
public class MusicalInstrumentEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    protected String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "playedMusicInstruments")
    protected Set<UserEntity> users;

    public MusicalInstrumentEntity() {

    }

    public MusicalInstrumentEntity(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }
}
