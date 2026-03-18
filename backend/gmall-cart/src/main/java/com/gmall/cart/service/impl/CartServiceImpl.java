package com.gmall.cart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gmall.cart.dto.AddToCartRequest;
import com.gmall.cart.dto.CartVO;
import com.gmall.cart.entity.Cart;
import com.gmall.cart.mapper.CartMapper;
import com.gmall.cart.service.CartService;
import com.gmall.product.dto.ProductVO;
import com.gmall.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 购物车服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartMapper cartMapper;
    private final ProductService productService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addToCart(Long userId, AddToCartRequest request) {
        log.info("添加商品到购物车，用户 ID: {}, 商品 ID: {}", userId, request.getProductId());

        // 查询商品详情
        ProductVO product = productService.getProductById(request.getProductId());
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }

        // 检查购物车中是否已有该商品
        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
                .eq(Cart::getProductId, request.getProductId());
        
        if (request.getSpecInfo() != null) {
            wrapper.eq(Cart::getSpecInfo, request.getSpecInfo());
        }

        Cart existCart = cartMapper.selectOne(wrapper);

        if (existCart != null) {
            // 已有该商品，数量累加
            existCart.setQuantity(existCart.getQuantity() + request.getQuantity());
            existCart.setUpdateTime(LocalDateTime.now());
            cartMapper.updateById(existCart);
            log.info("更新购物车商品数量，购物车 ID: {}", existCart.getId());
        } else {
            // 新增购物车记录
            Cart cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(request.getProductId());
            cart.setProductName(product.getName());
            cart.setProductImage(product.getImages());
            cart.setPrice(product.getPrice());
            cart.setQuantity(request.getQuantity());
            cart.setSpecInfo(request.getSpecInfo());
            cart.setChecked(1);
            cart.setCreateTime(LocalDateTime.now());
            cart.setUpdateTime(LocalDateTime.now());
            cartMapper.insert(cart);
            log.info("创建购物车记录，购物车 ID: {}", cart.getId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateQuantity(Long userId, Long cartId, Integer quantity) {
        log.info("更新购物车商品数量，用户 ID: {}, 购物车 ID: {}, 数量：{}", userId, cartId, quantity);

        Cart cart = cartMapper.selectById(cartId);
        if (cart == null) {
            throw new RuntimeException("购物车记录不存在");
        }

        if (!cart.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作该购物车记录");
        }

        cart.setQuantity(quantity);
        cart.setUpdateTime(LocalDateTime.now());
        cartMapper.updateById(cart);

        log.info("更新购物车商品数量成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeFromCart(Long userId, Long cartId) {
        log.info("删除购物车商品，用户 ID: {}, 购物车 ID: {}", userId, cartId);

        Cart cart = cartMapper.selectById(cartId);
        if (cart == null) {
            throw new RuntimeException("购物车记录不存在");
        }

        if (!cart.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作该购物车记录");
        }

        cartMapper.deleteById(cartId);
        log.info("删除购物车商品成功");
    }

    @Override
    public List<CartVO> getCartList(Long userId) {
        log.info("获取购物车列表，用户 ID: {}", userId);

        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
                .orderByDesc(Cart::getCreateTime);

        List<Cart> carts = cartMapper.selectList(wrapper);

        return carts.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CartVO> getCheckedCartList(Long userId) {
        log.info("获取选中购物车列表，用户 ID: {}", userId);

        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
                .eq(Cart::getChecked, 1)
                .orderByDesc(Cart::getCreateTime);

        List<Cart> carts = cartMapper.selectList(wrapper);

        return carts.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectAll(Long userId, Integer checked) {
        log.info("全选/取消全选，用户 ID: {}, 选中状态：{}", userId, checked);

        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId);

        List<Cart> carts = cartMapper.selectList(wrapper);
        for (Cart cart : carts) {
            cart.setChecked(checked);
            cart.setUpdateTime(LocalDateTime.now());
            cartMapper.updateById(cart);
        }

        log.info("全选/取消全选成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteChecked(Long userId) {
        log.info("删除选中商品，用户 ID: {}", userId);

        LambdaQueryWrapper<Cart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Cart::getUserId, userId)
                .eq(Cart::getChecked, 1);

        List<Cart> carts = cartMapper.selectList(wrapper);
        for (Cart cart : carts) {
            cartMapper.deleteById(cart.getId());
        }

        log.info("删除选中商品成功，删除数量：{}", carts.size());
    }

    /**
     * 转换为 VO
     */
    private CartVO convertToVO(Cart cart) {
        CartVO vo = new CartVO();
        BeanUtils.copyProperties(cart, vo);
        // 计算小计金额
        vo.setTotalPrice(cart.getPrice().multiply(new BigDecimal(cart.getQuantity())));
        return vo;
    }
}
