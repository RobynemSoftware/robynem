package com.robynem.mit.web.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.sql.Blob;

/**
 * Created by robyn_000 on 21/02/2016.
 */
@Entity
@Table(name = "mit_audio")
public class AudioEntity extends BaseEntity {

    @Column
    private String name;

    @Column
    @Lob
    private Blob file;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Blob getFile() {
        return file;
    }

    public void setFile(Blob file) {
        this.file = file;
    }
}
