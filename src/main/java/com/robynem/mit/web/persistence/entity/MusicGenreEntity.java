package com.robynem.mit.web.persistence.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by robyn_000 on 05/01/2016.
 */
@Entity
@Table(name = "mit_musicGenre")
@NamedQueries({
        @NamedQuery(name = "@HQL_GET_ALL_MUSIC_GENRES", query = "from MusicGenreEntity m order by m.name asc ")
})
public class MusicGenreEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    protected String name;

    public MusicGenreEntity() {

    }

    public MusicGenreEntity(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
