package com.robynem.mit.web.persistence.entity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by robyn_000 on 05/01/2016.
 */
@Entity
@Table(name = "mit_clubGenre")
@NamedQueries({
        @NamedQuery(name = "@HQL_GET_ALL_CLUB_GENRES", query = "from ClubGenreEntity cg order by cg.name asc ")
})
public class ClubGenreEntity extends BaseEntity {

    @Column(nullable = false, unique = true)
    protected String name;

    public ClubGenreEntity() {

    }

    public ClubGenreEntity(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
