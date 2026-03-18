package com.gmall.user.dto;

import lombok.Data;

/**
 * 登录响应 DTO
 */
@Data
public class LoginResponse {

    /**
     * 访问令牌
     */
    private String token;

    /**
     * 令牌类型
     */
    private String tokenType;

    /**
     * 用户信息
     */
    private UserVO user;

    public LoginResponse() {
    }

    public LoginResponse(String token, String tokenType, UserVO user) {
        this.token = token;
        this.tokenType = tokenType;
        this.user = user;
    }
}
