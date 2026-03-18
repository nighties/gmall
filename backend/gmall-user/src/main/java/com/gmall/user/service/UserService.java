package com.gmall.user.service;

import com.gmall.user.dto.LoginRequest;
import com.gmall.user.dto.LoginResponse;
import com.gmall.user.dto.RegisterRequest;
import com.gmall.user.dto.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param request 注册请求
     * @return 用户 ID
     */
    Long register(RegisterRequest request);

    /**
     * 用户登录
     *
     * @param request 登录请求
     * @return 登录响应 (包含 token 和用户信息)
     */
    LoginResponse login(LoginRequest request);

    /**
     * 根据 ID 获取用户信息
     *
     * @param userId 用户 ID
     * @return 用户信息
     */
    UserVO getUserById(Long userId);

    /**
     * 更新用户信息
     *
     * @param userId 用户 ID
     * @param userVO 用户信息
     */
    void updateUser(Long userId, UserVO userVO);
}
