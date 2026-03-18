package com.gmall.order.controller;

import com.gmall.Result;
import com.gmall.common.PageResult;
import com.gmall.order.dto.CreateOrderRequest;
import com.gmall.order.dto.OrderVO;
import com.gmall.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Api(tags = "订单管理")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    @ApiOperation("创建订单")
    public Result<String> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        String orderNo = orderService.createOrder(request);
        return Result.success("创建成功", orderNo);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据 ID 获取订单详情")
    public Result<OrderVO> getOrderById(@PathVariable Long id) {
        OrderVO orderVO = orderService.getOrderById(id);
        return Result.success(orderVO);
    }

    @GetMapping("/no/{orderNo}")
    @ApiOperation("根据订单号获取订单详情")
    public Result<OrderVO> getOrderByOrderNo(@PathVariable String orderNo) {
        OrderVO orderVO = orderService.getOrderByOrderNo(orderNo);
        return Result.success(orderVO);
    }

    @GetMapping("/list")
    @ApiOperation("分页查询订单列表")
    public Result<PageResult<List<OrderVO>>> listOrders(
            @RequestParam Long userId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<List<OrderVO>> result = orderService.listOrders(userId, status, pageNum, pageSize);
        return Result.success(result);
    }

    @PostMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result<Void> cancelOrder(
            @PathVariable Long id,
            @RequestParam Long userId) {
        orderService.cancelOrder(id, userId);
        return Result.success("取消成功");
    }

    @PostMapping("/confirm/{id}")
    @ApiOperation("确认收货")
    public Result<Void> confirmOrder(
            @PathVariable Long id,
            @RequestParam Long userId) {
        orderService.confirmOrder(id, userId);
        return Result.success("确认成功");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除订单")
    public Result<Void> deleteOrder(
            @PathVariable Long id,
            @RequestParam Long userId) {
        orderService.deleteOrder(id, userId);
        return Result.success("删除成功");
    }
}
