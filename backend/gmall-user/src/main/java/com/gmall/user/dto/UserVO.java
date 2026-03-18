package com.gmall.user.dto;

import lombok.Data;

/**
 * 用户信息响应 DTO
 */
@Data
public class UserVO {

    /**
     * 用户 ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像 URL
     */
    private String avatar;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别 0-未知 1-男 2-女
     */
    private Integer gender;

    /**
     * 积分
     */
    private Integer points;

    /**
     * 会员等级
     */
    private Integer level;
}
