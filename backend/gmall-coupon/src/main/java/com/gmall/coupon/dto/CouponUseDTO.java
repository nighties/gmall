package com.gmall.coupon.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CouponUseDTO {

    @NotNull(message = "用户优惠券 ID 不能为空")
    private Long userCouponId;

    @NotNull(message = "订单 ID 不能为空")
    private Long orderId;

    @NotNull(message = "用户 ID 不能为空")
    private Long userId;
}
