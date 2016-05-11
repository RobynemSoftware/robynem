package com.robynem.mit.web.model;

import java.io.Serializable;

/**
 * Created by robyn_000 on 01/05/2016.
 */
public class ValueTextModel<V,T> implements Serializable {

    private V value;

    private T text;

    public ValueTextModel() {

    }

    public ValueTextModel(V value, T text) {
        this.value = value;
        this.text = text;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public T getText() {
        return text;
    }

    public void setText(T text) {
        this.text = text;
    }
}
