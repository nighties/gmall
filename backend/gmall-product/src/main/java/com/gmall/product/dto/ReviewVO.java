package com.gmall.product.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewVO {

    private Long id;

    private Long userId;

    private String username;

    private String avatar;

    private Long productId;

    private Integer score;

    private String content;

    private String images;

    private String reply;

    private LocalDateTime createTime;
}
