package com.gmall.coupon.service;

import com.gmall.coupon.entity.Coupon;
import com.gmall.coupon.entity.UserCoupon;

import java.util.List;

public interface CouponService {

    List<Coupon> listAvailableCoupons(Long userId);

    UserCoupon receiveCoupon(Long couponId, Long userId);

    void useCoupon(Long userCouponId, Long orderId, Long userId);

    List<UserCoupon> listUserCoupons(Long userId, Integer status);
}
