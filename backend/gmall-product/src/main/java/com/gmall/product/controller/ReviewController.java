package com.gmall.product.controller;

import com.gmall.common.PageResult;
import com.gmall.common.Result;
import com.gmall.product.dto.ReviewAddRequest;
import com.gmall.product.dto.ReviewVO;
import com.gmall.product.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/review")
@Api(tags = "商品评价管理")
public class ReviewController {

    @Resource
    private ReviewService reviewService;

    @PostMapping("/add")
    @ApiOperation("提交商品评价")
    public Result<Void> addReview(@RequestBody @Validated ReviewAddRequest request) {
        reviewService.addReview(request);
        return Result.success(null);
    }

    @GetMapping("/list")
    @ApiOperation("查询商品评价列表")
    public Result<PageResult<ReviewVO>> listReviews(
            @RequestParam Long productId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        PageResult<ReviewVO> result = reviewService.listReviews(productId, page, size);
        return Result.success(result);
    }

    @GetMapping("/user/list")
    @ApiOperation("查询用户评价列表")
    public Result<PageResult<ReviewVO>> listUserReviews(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        PageResult<ReviewVO> result = reviewService.listUserReviews(userId, page, size);
        return Result.success(result);
    }
}
