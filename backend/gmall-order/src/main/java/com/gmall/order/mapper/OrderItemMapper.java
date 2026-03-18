package com.gmall.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gmall.order.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单商品 Mapper 接口
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

}
