package com.gmall.cart.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 更新购物车商品数量请求 DTO
 */
@Data
public class UpdateCartRequest {

    /**
     * 购物车 ID
     */
    @NotNull(message = "购物车 ID 不能为空")
    private Long cartId;

    /**
     * 商品数量
     */
    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "商品数量必须大于 0")
    private Integer quantity;
}
