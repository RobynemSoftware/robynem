package com.robynem.mit.web.persistence.entity;

import javax.persistence.*;
import java.sql.Blob;

/**
 * Created by robyn_000 on 03/01/2016.
 */
@Entity
@Table(name="mit_image")
@NamedQueries({

})
public class ImageEntity extends BaseEntity {

    @Column
    protected String filePath;

    @Column(columnDefinition = "mediumblob")
    @Lob
    protected Blob smallFile;

    @Column(columnDefinition = "mediumblob")
    @Lob
    protected Blob mediumFile;

    @Column(columnDefinition = "mediumblob")
    @Lob
    protected Blob largeFile;

    @Column(columnDefinition = "mediumblob")
    @Lob
    protected Blob originalFile;

    /**
     * Used to link published version to stage one
     */
    @Column
    protected Long linkId;


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Blob getSmallFile() {
        return smallFile;
    }

    public void setSmallFile(Blob smallFile) {
        this.smallFile = smallFile;
    }

    public Blob getMediumFile() {
        return mediumFile;
    }

    public void setMediumFile(Blob mediumFile) {
        this.mediumFile = mediumFile;
    }

    public Blob getLargeFile() {
        return largeFile;
    }

    public void setLargeFile(Blob largeFile) {
        this.largeFile = largeFile;
    }

    public Blob getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(Blob originalFile) {
        this.originalFile = originalFile;
    }

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }
}
