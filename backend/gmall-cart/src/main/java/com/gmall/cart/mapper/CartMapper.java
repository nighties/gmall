package com.gmall.cart.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gmall.cart.entity.Cart;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购物车 Mapper 接口
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {

}
