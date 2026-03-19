package com.gmall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gmall.common.util.JwtUtil;
import com.gmall.user.dto.LoginRequest;
import com.gmall.user.dto.LoginResponse;
import com.gmall.user.dto.RegisterRequest;
import com.gmall.user.dto.UserVO;
import com.gmall.user.entity.User;
import com.gmall.user.mapper.UserMapper;
import com.gmall.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND = "用户不存在";
    private static final String USER_DISABLED = "用户已被禁用";
    private static final String USERNAME_EXISTS = "用户名已存在";
    private static final String PASSWORD_ERROR = "密码错误";
    private static final String NO_PERMISSION = "无权操作该用户";

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Long register(RegisterRequest request) {
        log.info("用户注册，用户名：{}", request.getUsername());

        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername());
        User existUser = userMapper.selectOne(wrapper);
        if (existUser != null) {
            throw new RuntimeException(USERNAME_EXISTS);
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setGender(0);
        user.setPoints(0);
        user.setLevel(1);
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);
        log.info("用户注册成功，用户 ID: {}", user.getId());
        return user.getId();
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("用户登录，用户名：{}", request.getUsername());

        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, request.getUsername())
                .or()
                .eq(User::getPhone, request.getUsername());
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            throw new RuntimeException(USER_NOT_FOUND);
        }

        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException(PASSWORD_ERROR);
        }

        // 检查用户状态
        if (user.getStatus() == 0) {
            throw new RuntimeException(USER_DISABLED);
        }

        // 生成 token
        String token = JwtUtil.generateToken(user.getId());

        // 构建用户信息
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        userVO.setAvatar(user.getAvatar());
        userVO.setPhone(user.getPhone());
        userVO.setGender(user.getGender());
        userVO.setPoints(user.getPoints());
        userVO.setLevel(user.getLevel());

        log.info("用户登录成功，用户 ID: {}", user.getId());
        return new LoginResponse(token, "Bearer", userVO);
    }

    @Override
    public UserVO getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException(USER_NOT_FOUND);
        }

        return convertToUserVO(user);
    }

    @Override
    public void updateUser(Long userId, UserVO userVO) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new RuntimeException(USER_NOT_FOUND);
        }

        if (userVO.getNickname() != null) {
            user.setNickname(userVO.getNickname());
        }
        if (userVO.getAvatar() != null) {
            user.setAvatar(userVO.getAvatar());
        }
        if (userVO.getGender() != null) {
            user.setGender(userVO.getGender());
        }

        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
    }

    private UserVO convertToUserVO(User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setNickname(user.getNickname());
        userVO.setAvatar(user.getAvatar());
        userVO.setPhone(user.getPhone());
        userVO.setGender(user.getGender());
        userVO.setPoints(user.getPoints());
        userVO.setLevel(user.getLevel());
        return userVO;
    }
}
