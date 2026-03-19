package com.gmall.coupon.controller;

import com.gmall.common.Result;
import com.gmall.coupon.dto.CouponReceiveDTO;
import com.gmall.coupon.dto.CouponUseDTO;
import com.gmall.coupon.entity.Coupon;
import com.gmall.coupon.entity.UserCoupon;
import com.gmall.coupon.service.CouponService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    @Resource
    private CouponService couponService;

    @GetMapping("/list")
    public Result<List<Coupon>> listAvailableCoupons(@RequestParam Long userId) {
        List<Coupon> coupons = couponService.listAvailableCoupons(userId);
        return Result.success(coupons);
    }

    @PostMapping("/receive")
    public Result<UserCoupon> receiveCoupon(@RequestBody @Validated CouponReceiveDTO dto) {
        UserCoupon userCoupon = couponService.receiveCoupon(dto.getCouponId(), dto.getUserId());
        return Result.success(userCoupon);
    }

    @PostMapping("/use")
    public Result<Void> useCoupon(@RequestBody @Validated CouponUseDTO dto) {
        couponService.useCoupon(dto.getUserCouponId(), dto.getOrderId(), dto.getUserId());
        return Result.success(null);
    }

    @GetMapping("/user/list")
    public Result<List<UserCoupon>> listUserCoupons(
            @RequestParam Long userId,
            @RequestParam(required = false) Integer status) {
        List<UserCoupon> userCoupons = couponService.listUserCoupons(userId, status);
        return Result.success(userCoupons);
    }
}
