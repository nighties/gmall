package com.gmall.user.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PointHistoryVO {

    private Long id;

    private Integer points;

    private Integer changeType;

    private String description;

    private LocalDateTime createTime;
}
