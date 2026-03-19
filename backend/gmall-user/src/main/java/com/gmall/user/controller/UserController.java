package com.gmall.user.controller;

import com.gmall.Result;
import com.gmall.user.dto.LoginRequest;
import com.gmall.user.dto.LoginResponse;
import com.gmall.user.dto.RegisterRequest;
import com.gmall.user.dto.UserVO;
import com.gmall.user.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Api(tags = "用户管理")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public Result<Long> register(@Valid @RequestBody RegisterRequest request) {
        Long userId = userService.register(request);
        return Result.success("注册成功", userId);
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return Result.success("登录成功", response);
    }

    @GetMapping("/info")
    @ApiOperation("获取用户信息")
    public Result<UserVO> getUserInfo(@RequestParam Long userId, @RequestHeader("Authorization") String token) {
        Long currentUserId = parseUserIdFromToken(token);
        if (!currentUserId.equals(userId)) {
            throw new RuntimeException("无权访问");
        }
        UserVO userVO = userService.getUserById(userId);
        return Result.success(userVO);
    }

    @PutMapping("/update")
    @ApiOperation("更新用户信息")
    public Result<Void> updateUser(@RequestParam Long userId, @RequestBody UserVO userVO, @RequestHeader("Authorization") String token) {
        Long currentUserId = parseUserIdFromToken(token);
        if (!currentUserId.equals(userId)) {
            throw new RuntimeException("无权操作");
        }
        userService.updateUser(userId, userVO);
        return Result.success("更新成功");
    }

    private Long parseUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey("your-secret-key")
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody();
        return claims.get("userId", Long.class);
    }
}
