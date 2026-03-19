package com.gmall.product.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class ReviewAddRequest {

    @NotNull(message = "用户 ID 不能为空")
    private Long userId;

    @NotNull(message = "商品 ID 不能为空")
    private Long productId;

    @NotNull(message = "订单 ID 不能为空")
    private Long orderId;

    @NotNull(message = "评分不能为空")
    @Min(value = 1, message = "评分最低为 1 分")
    @Max(value = 5, message = "评分最高为 5 分")
    private Integer score;

    @NotBlank(message = "评价内容不能为空")
    private String content;

    private String images;
}
