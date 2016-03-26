package com.robynem.mit.web.persistence.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by robyn_000 on 26/03/2016.
 * Class to map dao custom results.
 * When a query needs to return a custom result rather than an entire object, its dao will return
 * this class. The objective is to create an implamentation of this class for each custom result.
 * The implementation should declare column alias constants.
 */
public class MapResult {

    protected List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

    protected int currentIndex = -1;

    public MapResult() {

    }

    public MapResult(List<Map<String, Object>> results) {
        this.results = results;
    }

    public void setResults(List<Map<String, Object>> results) {
        this.results = results;
    }

    public int getSize() {
        int size = 0;

        if (this.results != null) {
            size = this.results.size();
        }

        return size;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public boolean next() {
        boolean hasNext = false;

        if (this.getCurrentIndex() < (this.getSize() - 1)) {
            this.currentIndex++;
            hasNext = true;
        }

        return hasNext;
    }

    public <T> T get(String alias) {
        return (T) this.results.get(this.getCurrentIndex()).get(alias);
    }
}
