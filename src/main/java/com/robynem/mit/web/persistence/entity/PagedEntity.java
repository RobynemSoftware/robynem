package com.robynem.mit.web.persistence.entity;

import java.util.List;

/**
 * Created by robyn_000 on 08/03/2016.
 */
public class PagedEntity<T extends BaseEntity> {

    protected long totalRows;

    protected List<T> results;

    /**
     * Gets or sets numer of rows in the previous page
     */
    protected long previousPageRows;

    /**
     * Gets or sets numer of rows in the next page
     */
    protected long nextPageRows;

    protected int currentPage;

    protected int totalPages;


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

    public long getPreviousPageRows() {
        return previousPageRows;
    }

    public void setPreviousPageRows(long previousPageRows) {
        this.previousPageRows = previousPageRows;
    }

    public long getNextPageRows() {
        return nextPageRows;
    }

    public void setNextPageRows(long nextPageRows) {
        this.nextPageRows = nextPageRows;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
