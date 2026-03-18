package com.gmall.order.service;

import com.gmall.common.PageResult;
import com.gmall.order.dto.CreateOrderRequest;
import com.gmall.order.dto.OrderVO;

import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param request 创建订单请求
     * @return 订单号
     */
    String createOrder(CreateOrderRequest request);

    /**
     * 根据 ID 获取订单详情
     *
     * @param orderId 订单 ID
     * @return 订单详情
     */
    OrderVO getOrderById(Long orderId);

    /**
     * 根据订单号获取订单详情
     *
     * @param orderNo 订单号
     * @return 订单详情
     */
    OrderVO getOrderByOrderNo(String orderNo);

    /**
     * 分页查询订单列表
     *
     * @param userId 用户 ID
     * @param status 订单状态
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @return 订单列表
     */
    PageResult<List<OrderVO>> listOrders(Long userId, Integer status, Integer pageNum, Integer pageSize);

    /**
     * 取消订单
     *
     * @param orderId 订单 ID
     * @param userId 用户 ID
     */
    void cancelOrder(Long orderId, Long userId);

    /**
     * 确认收货
     *
     * @param orderId 订单 ID
     * @param userId 用户 ID
     */
    void confirmOrder(Long orderId, Long userId);

    /**
     * 删除订单
     *
     * @param orderId 订单 ID
     * @param userId 用户 ID
     */
    void deleteOrder(Long orderId, Long userId);
}
