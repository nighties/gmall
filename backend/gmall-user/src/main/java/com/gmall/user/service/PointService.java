package com.gmall.user.service;

import com.gmall.user.dto.PointHistoryVO;

import java.util.List;

public interface PointService {

    Integer getPointBalance(Long userId);

    List<PointHistoryVO> getPointHistory(Long userId, Integer page, Integer size);
}
