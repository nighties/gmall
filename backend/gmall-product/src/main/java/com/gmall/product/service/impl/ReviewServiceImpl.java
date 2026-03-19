package com.gmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gmall.common.PageResult;
import com.gmall.product.dto.ReviewAddRequest;
import com.gmall.product.dto.ReviewVO;
import com.gmall.product.entity.Review;
import com.gmall.product.mapper.ReviewMapper;
import com.gmall.product.service.ReviewService;
import com.gmall.user.entity.User;
import com.gmall.user.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private static final String ORDER_ALREADY_REVIEWED = "该订单已评价过";
    private static final String USER_NOT_FOUND = "用户不存在";

    @Resource
    private ReviewMapper reviewMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public void addReview(ReviewAddRequest request) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getOrderId, request.getOrderId());
        wrapper.eq(Review::getUserId, request.getUserId());
        Review existingReview = reviewMapper.selectOne(wrapper);
        if (existingReview != null) {
            throw new RuntimeException(ORDER_ALREADY_REVIEWED);
        }

        User user = userMapper.selectById(request.getUserId());
        if (user == null) {
            throw new RuntimeException(USER_NOT_FOUND);
        }

        Review review = new Review();
        review.setUserId(request.getUserId());
        review.setProductId(request.getProductId());
        review.setOrderId(request.getOrderId());
        review.setScore(request.getScore());
        review.setContent(request.getContent());
        review.setImages(request.getImages());
        review.setStatus(1);
        review.setCreateTime(LocalDateTime.now());
        review.setUpdateTime(LocalDateTime.now());

        reviewMapper.insert(review);
    }

    @Override
    public PageResult<ReviewVO> listReviews(Long productId, Integer page, Integer size) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getProductId, productId);
        wrapper.eq(Review::getStatus, 1);
        wrapper.orderByDesc(Review::getCreateTime);

        long total = reviewMapper.selectCount(wrapper);

        List<ReviewVO> reviewVOList = java.util.Collections.emptyList();
        if (page != null && size != null) {
            wrapper.last("LIMIT " + ((page - 1) * size) + ", " + size);
            List<Review> reviewList = reviewMapper.selectList(wrapper);
            reviewVOList = reviewList.stream().map(review -> {
                ReviewVO vo = new ReviewVO();
                BeanUtils.copyProperties(review, vo);

                User user = userMapper.selectById(review.getUserId());
                if (user != null) {
                    vo.setUsername(user.getUsername());
                    vo.setAvatar(user.getAvatar());
                }

                return vo;
            }).collect(Collectors.toList());
        }

        return new PageResult<>((long) page, (long) size, total, reviewVOList);
    }

    @Override
    public PageResult<ReviewVO> listUserReviews(Long userId, Integer page, Integer size) {
        LambdaQueryWrapper<Review> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Review::getUserId, userId);
        wrapper.orderByDesc(Review::getCreateTime);

        long total = reviewMapper.selectCount(wrapper);

        List<ReviewVO> reviewVOList = java.util.Collections.emptyList();
        if (page != null && size != null) {
            wrapper.last("LIMIT " + ((page - 1) * size) + ", " + size);
            List<Review> reviewList = reviewMapper.selectList(wrapper);
            reviewVOList = reviewList.stream().map(review -> {
                ReviewVO vo = new ReviewVO();
                BeanUtils.copyProperties(review, vo);

                User user = userMapper.selectById(review.getUserId());
                if (user != null) {
                    vo.setUsername(user.getUsername());
                    vo.setAvatar(user.getAvatar());
                }

                return vo;
            }).collect(Collectors.toList());
        }

        return new PageResult<>((long) page, (long) size, total, reviewVOList);
    }
}
