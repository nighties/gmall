package com.gmall.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gmall.coupon.entity.Coupon;
import com.gmall.coupon.entity.UserCoupon;
import com.gmall.coupon.mapper.CouponMapper;
import com.gmall.coupon.mapper.UserCouponMapper;
import com.gmall.coupon.service.CouponService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CouponServiceImpl implements CouponService {

    private static final String COUPON_NOT_FOUND = "优惠券不存在";
    private static final String COUPON_EXHAUSTED = "优惠券已领完";
    private static final String COUPON_NOT_AVAILABLE = "优惠券不可领取";
    private static final String USER_COUPON_NOT_FOUND = "用户优惠券不存在";
    private static final String COUPON_ALREADY_USED = "优惠券已使用";
    private static final String NO_PERMISSION = "无权操作该优惠券";
    private static final String COUPON_ALREADY_RECEIVED = "您已领取过该优惠券";

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private UserCouponMapper userCouponMapper;

    @Override
    public List<Coupon> listAvailableCoupons(Long userId) {
        LambdaQueryWrapper<Coupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Coupon::getStatus, 1);
        wrapper.gt(Coupon::getRemainCount, 0);
        wrapper.le(Coupon::getStartTime, LocalDateTime.now());
        wrapper.gt(Coupon::getEndTime, LocalDateTime.now());
        return couponMapper.selectList(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserCoupon receiveCoupon(Long couponId, Long userId) {
        Coupon coupon = couponMapper.selectById(couponId);
        if (coupon == null) {
            throw new RuntimeException(COUPON_NOT_FOUND);
        }

        if (coupon.getRemainCount() <= 0) {
            throw new RuntimeException(COUPON_EXHAUSTED);
        }

        if (coupon.getStatus() != 1) {
            throw new RuntimeException(COUPON_NOT_AVAILABLE);
        }

        if (LocalDateTime.now().isBefore(coupon.getStartTime()) || 
            LocalDateTime.now().isAfter(coupon.getEndTime())) {
            throw new RuntimeException(COUPON_NOT_AVAILABLE);
        }

        LambdaQueryWrapper<UserCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCoupon::getCouponId, couponId);
        wrapper.eq(UserCoupon::getUserId, userId);
        wrapper.in(UserCoupon::getStatus, 0, 1);
        UserCoupon existingUserCoupon = userCouponMapper.selectOne(wrapper);
        if (existingUserCoupon != null) {
            throw new RuntimeException(COUPON_ALREADY_RECEIVED);
        }

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserId(userId);
        userCoupon.setCouponId(couponId);
        userCoupon.setStatus(0);
        userCoupon.setCreateTime(LocalDateTime.now());
        userCoupon.setUpdateTime(LocalDateTime.now());

        userCouponMapper.insert(userCoupon);

        coupon.setRemainCount(coupon.getRemainCount() - 1);
        couponMapper.updateById(coupon);

        return userCoupon;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void useCoupon(Long userCouponId, Long orderId, Long userId) {
        UserCoupon userCoupon = userCouponMapper.selectById(userCouponId);
        if (userCoupon == null) {
            throw new RuntimeException(USER_COUPON_NOT_FOUND);
        }

        if (!userCoupon.getUserId().equals(userId)) {
            throw new RuntimeException(NO_PERMISSION);
        }

        if (userCoupon.getStatus() != 0) {
            throw new RuntimeException(COUPON_ALREADY_USED);
        }

        userCoupon.setStatus(1);
        userCoupon.setOrderId(orderId);
        userCoupon.setUseTime(LocalDateTime.now());
        userCoupon.setUpdateTime(LocalDateTime.now());

        userCouponMapper.updateById(userCoupon);
    }

    @Override
    public List<UserCoupon> listUserCoupons(Long userId, Integer status) {
        LambdaQueryWrapper<UserCoupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCoupon::getUserId, userId);
        if (status != null) {
            wrapper.eq(UserCoupon::getStatus, status);
        }
        wrapper.orderByDesc(UserCoupon::getCreateTime);
        return userCouponMapper.selectList(wrapper);
    }
}
