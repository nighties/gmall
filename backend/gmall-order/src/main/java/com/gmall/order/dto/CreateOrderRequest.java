package com.gmall.order.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 创建订单请求 DTO
 */
@Data
public class CreateOrderRequest {

    /**
     * 用户 ID
     */
    @NotNull(message = "用户 ID 不能为空")
    private Long userId;

    /**
     * 购物车 ID 列表
     */
    @NotEmpty(message = "请选择商品")
    private List<Long> cartIds;

    /**
     * 收货地址 ID
     */
    @NotNull(message = "请选择收货地址")
    private Long addressId;

    /**
     * 优惠券 ID
     */
    private Long couponId;

    /**
     * 订单备注
     */
    private String remark;
}
