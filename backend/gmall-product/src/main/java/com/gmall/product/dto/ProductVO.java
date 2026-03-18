package com.gmall.product.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品信息响应 DTO
 */
@Data
public class ProductVO {

    /**
     * 商品 ID
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 价格 (分)
     */
    private BigDecimal price;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 分类 ID
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 商家 ID
     */
    private Long shopId;

    /**
     * 商家名称
     */
    private String shopName;

    /**
     * 商品图片 (JSON 数组)
     */
    private String images;

    /**
     * 商品规格 (JSON)
     */
    private String specs;

    /**
     * 状态 0-下架 1-上架
     */
    private Integer status;
}
