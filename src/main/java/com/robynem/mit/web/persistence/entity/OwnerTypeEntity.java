package com.robynem.mit.web.persistence.entity;

import javax.persistence.*;

/**
 * Created by robyn_000 on 09/01/2016.
 */
@Entity
@Table(name = "mit_ownerType")
@NamedQueries({
        @NamedQuery(name = "@HQL_GET_OWNER_TYPE_BY_CODE", query = "from OwnerTypeEntity ot where ot.code = :code")
})
public class OwnerTypeEntity extends BaseEntity {

    @Column(unique = true)
    protected String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
