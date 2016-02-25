package com.robynem.mit.web.persistence.entity;

import javax.persistence.*;

/**
 * Created by robyn_000 on 09/01/2016.
 */
@Entity
@Table(name = "mit_entityStatus")
@NamedQueries({
        @NamedQuery(name = "@HQL_GET_ENTITY_STATUS_BY_CODE", query = "from EntityStatusEntity es where es.code = :code")
})
public class EntityStatusEntity extends BaseEntity {

    @Column
    protected String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}


