package com.gmall.product.service;

import com.gmall.common.PageResult;
import com.gmall.product.dto.ReviewAddRequest;
import com.gmall.product.dto.ReviewVO;

public interface ReviewService {

    void addReview(ReviewAddRequest request);

    PageResult<ReviewVO> listReviews(Long productId, Integer page, Integer size);

    PageResult<ReviewVO> listUserReviews(Long userId, Integer page, Integer size);
}
