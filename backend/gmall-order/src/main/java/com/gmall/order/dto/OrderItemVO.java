package com.gmall.order.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单商品响应 DTO
 */
@Data
public class OrderItemVO {

    /**
     * 订单商品 ID
     */
    private Long id;

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
     * 小计金额
     */
    private BigDecimal totalPrice;
}
