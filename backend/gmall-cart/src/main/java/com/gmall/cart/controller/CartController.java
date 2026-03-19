package com.gmall.cart.controller.cart;

import com.gmall.common.Result;
import com.gmall.cart.dto.AddToCartRequest;
import com.gmall.cart.dto.CartVO;
import com.gmall.cart.dto.UpdateCartRequest;
import com.gmall.cart.service.CartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 购物车控制器
 */
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@Api(tags = "购物车管理")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    @ApiOperation("添加商品到购物车")
    public Result<Void> addToCart(
            @RequestParam Long userId,
            @Valid @RequestBody AddToCartRequest request) {
        cartService.addToCart(userId, request);
        return Result.success(null);
    }

    @PutMapping("/update")
    @ApiOperation("更新购物车商品数量")
    public Result<Void> updateQuantity(
            @RequestParam Long userId,
            @Valid @RequestBody UpdateCartRequest request) {
        cartService.updateQuantity(userId, request.getCartId(), request.getQuantity());
        return Result.success(null);
    }

    @DeleteMapping("/remove/{cartId}")
    @ApiOperation("删除购物车商品")
    public Result<Void> removeFromCart(
            @RequestParam Long userId,
            @PathVariable Long cartId) {
        cartService.removeFromCart(userId, cartId);
        return Result.success(null);
    }

    @GetMapping("/list")
    @ApiOperation("获取购物车列表")
    public Result<List<CartVO>> getCartList(@RequestParam Long userId) {
        List<CartVO> list = cartService.getCartList(userId);
        return Result.success(list);
    }

    @GetMapping("/checked")
    @ApiOperation("获取选中购物车列表")
    public Result<List<CartVO>> getCheckedCartList(@RequestParam Long userId) {
        List<CartVO> list = cartService.getCheckedCartList(userId);
        return Result.success(list);
    }

    @PutMapping("/select-all")
    @ApiOperation("全选/取消全选")
    public Result<Void> selectAll(
            @RequestParam Long userId,
            @RequestParam Integer checked) {
        cartService.selectAll(userId, checked);
        return Result.success(null);
    }

    @DeleteMapping("/delete-checked")
    @ApiOperation("删除选中商品")
    public Result<Void> deleteChecked(@RequestParam Long userId) {
        cartService.deleteChecked(userId);
        return Result.success(null);
    }
}
