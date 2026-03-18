package com.gmall.cart.service;

import com.gmall.cart.dto.AddToCartRequest;
import com.gmall.cart.dto.CartVO;

import java.util.List;

/**
 * 购物车服务接口
 */
public interface CartService {

    /**
     * 添加商品到购物车
     *
     * @param userId 用户 ID
     * @param request 请求
     */
    void addToCart(Long userId, AddToCartRequest request);

    /**
     * 更新购物车商品数量
     *
     * @param userId 用户 ID
     * @param cartId 购物车 ID
     * @param quantity 数量
     */
    void updateQuantity(Long userId, Long cartId, Integer quantity);

    /**
     * 删除购物车商品
     *
     * @param userId 用户 ID
     * @param cartId 购物车 ID
     */
    void removeFromCart(Long userId, Long cartId);

    /**
     * 获取购物车列表
     *
     * @param userId 用户 ID
     * @return 购物车列表
     */
    List<CartVO> getCartList(Long userId);

    /**
     * 获取购物车选中商品列表
     *
     * @param userId 用户 ID
     * @return 购物车列表
     */
    List<CartVO> getCheckedCartList(Long userId);

    /**
     * 全选/取消全选
     *
     * @param userId 用户 ID
     * @param checked 是否选中
     */
    void selectAll(Long userId, Integer checked);

    /**
     * 删除选中的商品
     *
     * @param userId 用户 ID
     */
    void deleteChecked(Long userId);
}
