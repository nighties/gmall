package com.gmall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gmall.cart.dto.CartVO;
import com.gmall.cart.service.CartService;
import com.gmall.common.PageResult;
import com.gmall.order.dto.AddressVO;
import com.gmall.order.dto.CreateOrderRequest;
import com.gmall.order.dto.OrderItemVO;
import com.gmall.order.dto.OrderVO;
import com.gmall.order.entity.Order;
import com.gmall.order.entity.OrderItem;
import com.gmall.order.mapper.OrderItemMapper;
import com.gmall.order.mapper.OrderMapper;
import com.gmall.order.service.OrderService;
import com.gmall.order.util.OrderNoGenerator;
import com.gmall.user.dto.UserVO;
import com.gmall.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartService cartService;
    private final UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(CreateOrderRequest request) {
        log.info("创建订单，用户 ID: {}", request.getUserId());

        // 1. 获取购物车选中商品
        List<CartVO> cartList = cartService.getCheckedCartList(request.getUserId());
        if (cartList == null || cartList.isEmpty()) {
            throw new RuntimeException("购物车为空");
        }

        // 2. 计算订单金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartVO cart : cartList) {
            BigDecimal itemTotal = cart.getPrice().multiply(new BigDecimal(cart.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            OrderItem item = new OrderItem();
            item.setProductId(cart.getProductId());
            item.setProductName(cart.getProductName());
            item.setProductImage(cart.getProductImage());
            item.setPrice(cart.getPrice());
            item.setQuantity(cart.getQuantity());
            item.setSpecInfo(cart.getSpecInfo());
            orderItems.add(item);
        }

        // 3. 创建订单
        String orderNo = OrderNoGenerator.generate();
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(request.getUserId());
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount); // 暂未计算运费和优惠
        order.setFreightAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setStatus(0); // 待付款
        order.setRemark(request.getRemark());
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.insert(order);

        // 4. 创建订单商品
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            item.setCreateTime(LocalDateTime.now());
            orderItemMapper.insert(item);
        }

        // 5. 删除购物车选中商品
        cartService.deleteChecked(request.getUserId());

        log.info("创建订单成功，订单号：{}", orderNo);
        return orderNo;
    }

    @Override
    public OrderVO getOrderById(Long orderId) {
        log.info("查询订单详情，订单 ID: {}", orderId);

        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        return convertToVO(order);
    }

    @Override
    public OrderVO getOrderByOrderNo(String orderNo) {
        log.info("查询订单详情，订单号：{}", orderNo);

        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getOrderNo, orderNo);
        Order order = orderMapper.selectOne(wrapper);

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        return convertToVO(order);
    }

    @Override
    public PageResult<OrderVO> listOrders(Long userId, Integer status, Integer pageNum, Integer pageSize) {
        log.info("查询订单列表，用户 ID: {}, 状态：{}", userId, status);

        Page<Order> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, userId);
        
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        
        wrapper.orderByDesc(Order::getCreateTime);

        Page<Order> orderPage = orderMapper.selectPage(page, wrapper);

        List<OrderVO> orderVOList = orderPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return new PageResult<OrderVO>(
                orderPage.getCurrent(),
                orderPage.getSize(),
                orderPage.getTotal(),
                orderVOList
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, Long userId) {
        log.info("取消订单，订单 ID: {}, 用户 ID: {}", orderId, userId);

        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作该订单");
        }

        if (order.getStatus() != 0) {
            throw new RuntimeException("只有待付款订单才能取消");
        }

        order.setStatus(4); // 已取消
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);

        log.info("取消订单成功，订单 ID: {}", orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmOrder(Long orderId, Long userId) {
        log.info("确认收货，订单 ID: {}, 用户 ID: {}", orderId, userId);

        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作该订单");
        }

        if (order.getStatus() != 2) {
            throw new RuntimeException("只有待收货订单才能确认收货");
        }

        order.setStatus(3); // 已完成
        order.setReceiveTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        orderMapper.updateById(order);

        // TODO: 增加用户积分

        log.info("确认收货成功，订单 ID: {}", orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrder(Long orderId, Long userId) {
        log.info("删除订单，订单 ID: {}, 用户 ID: {}", orderId, userId);

        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        if (!order.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作该订单");
        }

        orderMapper.deleteById(orderId);

        log.info("删除订单成功，订单 ID: {}", orderId);
    }

    /**
     * 转换为 VO
     */
    private OrderVO convertToVO(Order order) {
        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);

        // 查询订单商品
        LambdaQueryWrapper<OrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderItem::getOrderId, order.getId());
        List<OrderItem> items = orderItemMapper.selectList(wrapper);

        List<OrderItemVO> itemVOList = items.stream()
                .map(item -> {
                    OrderItemVO itemVO = new OrderItemVO();
                    BeanUtils.copyProperties(item, itemVO);
                    itemVO.setTotalPrice(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
                    return itemVO;
                })
                .collect(Collectors.toList());

        vo.setItems(itemVOList);

        // TODO: 查询收货地址信息
        // vo.setAddress(address);

        return vo;
    }
}
