package com.frontier.job.admin.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.ibatis.session.RowBounds;

/**
 * 分页请求数据结构
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageRequest {

    private final static int DEFAULT_PAGE_NO = 1;
    private final static int DEFAULT_PAGE_SIZE = 10;

    @JsonInclude private int pageNo;

    @JsonInclude private int pageSize;

    public RowBounds getRowBounds(){
        return new RowBounds((pageNo - 1) * pageSize, pageSize);
    }

    public PageRequest() {
        this(1, DEFAULT_PAGE_SIZE);
    }

    public PageRequest(int pageNo, int pageSize) {
        this.pageNo = pageNo <= 0 ? DEFAULT_PAGE_NO : pageNo;
        this.pageSize = pageSize <= 0 ? DEFAULT_PAGE_SIZE : pageSize;
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

}

