package com.gmall.common;

import lombok.Data;

/**
 * 分页结果类
 */
@Data
public class PageResult<T> {

    /**
     * 当前页码
     */
    private Long pageNum;

    /**
     * 每页记录数
     */
    private Long pageSize;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页数
     */
    private Long pages;

    /**
     * 数据列表
     */
    private java.util.List<T> list;

    public PageResult() {
    }

    public PageResult(Long pageNum, Long pageSize, Long total, java.util.List<T> list) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.list = list;
        this.pages = (total + pageSize - 1) / pageSize;
    }
}
