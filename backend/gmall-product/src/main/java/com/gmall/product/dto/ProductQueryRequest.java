package com.gmall.product.dto;

import lombok.Data;

/**
 * 商品查询请求 DTO
 */
@Data
public class ProductQueryRequest {

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页记录数
     */
    private Integer pageSize = 10;

    /**
     * 分类 ID
     */
    private Long categoryId;

    /**
     * 商家 ID
     */
    private Long shopId;

    /**
     * 价格区间 (最低)
     */
    private BigDecimal minPrice;

    /**
     * 价格区间 (最高)
     */
    private BigDecimal maxPrice;

    /**
     * 排序字段 (price, sales, create_time)
     */
    private String sortBy;

    /**
     * 排序方式 (asc, desc)
     */
    private String sortOrder = "desc";

    /**
     * 关键词搜索
     */
    private String keyword;
}
