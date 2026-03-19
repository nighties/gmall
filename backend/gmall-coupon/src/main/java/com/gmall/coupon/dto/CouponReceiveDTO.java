package com.gmall.coupon.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CouponReceiveDTO {

    @NotNull(message = "优惠券 ID 不能为空")
    private Long couponId;

    @NotNull(message = "用户 ID 不能为空")
    private Long userId;
}
