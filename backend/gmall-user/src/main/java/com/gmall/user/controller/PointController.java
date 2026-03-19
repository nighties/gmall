package com.gmall.user.controller;

import com.gmall.common.Result;
import com.gmall.user.dto.PointHistoryVO;
import com.gmall.user.service.PointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/point")
@Api(tags = "积分管理")
public class PointController {

    @Resource
    private PointService pointService;

    @GetMapping("/balance")
    @ApiOperation("查询积分余额")
    public Result<Integer> getPointBalance(@RequestParam Long userId) {
        Integer points = pointService.getPointBalance(userId);
        return Result.success(points);
    }

    @GetMapping("/history")
    @ApiOperation("查询积分明细")
    public Result<List<PointHistoryVO>> getPointHistory(
            @RequestParam Long userId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<PointHistoryVO> history = pointService.getPointHistory(userId, page, size);
        return Result.success(history);
    }
}
