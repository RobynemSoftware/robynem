package com.robynem.mit.web.model;

import java.io.Serializable;

/**
 * Created by robyn_000 on 30/01/2016.
 */
public class ContactModel implements Serializable {

    protected Long id;

    protected String value;

    public ContactModel() {
    }

    public ContactModel(Long id) {
        this.id = id;
    }

    public ContactModel(String value) {
        this.value = value;
    }

    public ContactModel(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
