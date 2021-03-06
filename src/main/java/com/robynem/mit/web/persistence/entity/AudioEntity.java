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

    @Column
    private String fileName;

    @Column
    private String soundCloudUrl;

    @Column
    protected Long linkId;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSoundCloudUrl() {
        return soundCloudUrl;
    }

    public void setSoundCloudUrl(String soundCloudUrl) {
        this.soundCloudUrl = soundCloudUrl;
    }

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }
}
