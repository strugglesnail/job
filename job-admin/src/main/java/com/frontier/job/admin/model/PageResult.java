package com.frontier.job.admin.model;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 分页结果数据结构
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResult<T> {

    private final static int DEFAULT_PAGE_SIZE = 10;

    @JsonInclude private Integer pageNo;

    @JsonInclude private Integer pageSize;

    @JsonInclude private Integer totalPage;

    @JsonInclude private Integer total;

    @JsonInclude private T data;

    public PageResult() {

    }

    public PageResult(T data) {
        this.data = data;
    }

    public PageResult(Integer pageNo, Integer pageSize) {
        this.pageNo = Math.max(pageNo, 0);
        this.pageSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
    }

    public PageResult(int pageNo, int pageSize, int total) {
        this(pageNo, pageSize);
        this.total = total;
        this.totalPage = totalPage(total, pageSize);
    }

    public PageResult(int page, int pageSize, int total, T data) {
        this(page, pageSize, total);
        this.data = data;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static int totalPage(int totalCount, int pageSize) {
        if (pageSize == 0) {
            return 0;
        } else {
            return totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        }
    }
}

