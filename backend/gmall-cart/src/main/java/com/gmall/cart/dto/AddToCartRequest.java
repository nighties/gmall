package com.gmall.cart.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 加入购物车请求 DTO
 */
@Data
public class AddToCartRequest {

    /**
     * 商品 ID
     */
    @NotNull(message = "商品 ID 不能为空")
    private Long productId;

    /**
     * 商品数量
     */
    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "商品数量必须大于 0")
    private Integer quantity;

    /**
     * 规格信息
     */
    private String specInfo;
}
