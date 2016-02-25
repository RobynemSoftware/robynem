package com.robynem.mit.web.util;

/**
 * Created by robyn_000 on 23/01/2016.
 */
public enum EditBandTabIndex {
    GENERAL(0),
    COMPONENTS(1),
    MEDIA(2);

    private int value;

    private EditBandTabIndex(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static EditBandTabIndex fromInt(int value) {
        return EditBandTabIndex.values()[value];
    }


}
