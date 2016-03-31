package com.robynem.mit.web.persistence.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by roberto on 11/12/2015.
 */
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    protected Long id;

    @Column(nullable=true)
    protected Date created;

    @Column(nullable=true)
    protected Date updated;

    /*@Column(nullable=false, columnDefinition = "TINYINT DEFAULT 0")
    protected boolean deleted;*/

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseEntity) {
            BaseEntity other = (BaseEntity)obj;
            if (this.id == null || other.id == null) {
                return super.equals(other);
            } else {
                return this.id.equals(other.id);
            }
        } else {
            return super.equals(obj);
        }


    }
}
