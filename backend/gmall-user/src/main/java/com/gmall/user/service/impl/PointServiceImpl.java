package com.gmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gmall.user.dto.PointHistoryVO;
import com.gmall.user.entity.User;
import com.gmall.user.entity.UserPointHistory;
import com.gmall.user.mapper.UserMapper;
import com.gmall.user.mapper.UserPointHistoryMapper;
import com.gmall.user.service.PointService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointServiceImpl implements PointService {

    private static final String USER_NOT_FOUND = "用户不存在";

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserPointHistoryMapper userPointHistoryMapper;

    @Override
    public Integer getPointBalance(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException(USER_NOT_FOUND);
        }
        return user.getPoints();
    }

    @Override
    public List<PointHistoryVO> getPointHistory(Long userId, Integer page, Integer size) {
        LambdaQueryWrapper<UserPointHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPointHistory::getUserId, userId);
        wrapper.orderByDesc(UserPointHistory::getCreateTime);
        
        if (page != null && size != null) {
            wrapper.last("LIMIT " + ((page - 1) * size) + ", " + size);
        }
        
        List<UserPointHistory> historyList = userPointHistoryMapper.selectList(wrapper);
        return historyList.stream().map(history -> {
            PointHistoryVO vo = new PointHistoryVO();
            BeanUtils.copyProperties(history, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}
