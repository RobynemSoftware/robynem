package com.robynem.mit.web.persistence.criteria;

/**
 * Created by rrennaloiacono on 31/03/2016.
 */
public class BaseCriteria {

    protected Integer pageSize = Integer.MAX_VALUE;

    protected Integer currentPage = 1;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }
}
