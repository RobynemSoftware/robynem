package com.robynem.mit.web.persistence.entity;

import javax.persistence.*;

/**
 * Created by robyn_000 on 05/03/2016.
 */
@Entity
@Table(name = "mit_notificationType")
@NamedQueries({
        @NamedQuery(name = "@HQL_GET_NOTIFICATION_TYPE_BY_CODE", query = "from NotificationTypeEntity nt where nt.code = :code")
})
public class NotificationTypeEntity extends BaseEntity {

    @Column
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
