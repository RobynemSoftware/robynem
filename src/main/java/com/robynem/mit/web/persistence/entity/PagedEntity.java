package com.robynem.mit.web.persistence.entity;

import java.util.List;

/**
 * Created by robyn_000 on 08/03/2016.
 */
public class PagedEntity<T extends BaseEntity> {

    private long totalRows;

    private List<T> results;

    /**
     * Gets or sets numer of rows before current page
     */
    private long rowsBefore;

    /**
     * Gets or sets numer of rows after current page
     */
    private long rowsAfter;

    private int currentPage;

    public long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(long totalRows) {
        this.totalRows = totalRows;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public long getRowsBefore() {
        return rowsBefore;
    }

    public void setRowsBefore(long rowsBefore) {
        this.rowsBefore = rowsBefore;
    }

    public long getRowsAfter() {
        return rowsAfter;
    }

    public void setRowsAfter(long rowsAfter) {
        this.rowsAfter = rowsAfter;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
