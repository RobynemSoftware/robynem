package com.robynem.mit.web.persistence.entity;

import java.util.List;

/**
 * Created by robyn_000 on 08/03/2016.
 */
public class PagedEntity<T> {

    protected long totalRows;

    protected List<T> results;

    /**
     * Gets or sets numer of rows in the previous page
     */
    protected int previousPageRows;

    /**
     * Gets or sets numer of rows in the next page
     */
    protected int nextPageRows;

    protected int currentPage;

    protected int totalPages;

    protected int pageSize;


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

    public int getPreviousPageRows() {
        return previousPageRows;
    }

    public void setPreviousPageRows(int previousPageRows) {
        this.previousPageRows = previousPageRows;
    }

    public int getNextPageRows() {
        return nextPageRows;
    }

    public void setNextPageRows(int nextPageRows) {
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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Returns first result for a paged query.
     * @return
     */
    public int getCurrentFirstResult() {
        return this.pageSize * (this.currentPage - 1);
    }
}
