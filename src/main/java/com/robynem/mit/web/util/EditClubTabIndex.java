package com.robynem.mit.web.util;

/**
 * Created by robyn_000 on 18/04/2016.
 */
public enum EditClubTabIndex {
    GENERAL(0),
    MEDIA(1);

    private int value;

    private EditClubTabIndex(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static EditClubTabIndex fromInt(int value) {
        return EditClubTabIndex.values()[value];
    }
}
