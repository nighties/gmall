package com.gmall.cart.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车信息响应 DTO
 */
@Data
public class CartVO {

    /**
     * 购物车 ID
     */
    private Long id;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 商品 ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品图片
     */
    private String productImage;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 规格信息
     */
    private String specInfo;

    /**
     * 是否选中 0-否 1-是
     */
    private Integer checked;

    /**
     * 小计金额
     */
    private BigDecimal totalPrice;
}
