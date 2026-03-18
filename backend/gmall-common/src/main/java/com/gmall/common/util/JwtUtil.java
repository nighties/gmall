package com.gmall.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
public class JwtUtil {

    /**
     * 密钥
     */
    private static String SECRET = "gmall-secret-key-2026-meituan-miniprogram";

    /**
     * 过期时间 (毫秒)
     */
    private static long EXPIRATION = 86400000L; // 24 小时

    /**
     * 生成令牌
     *
     * @param userId 用户 ID
     * @return 令牌
     */
    public static String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        return createToken(claims, String.valueOf(userId));
    }

    /**
     * 创建令牌
     *
     * @param claims 声明
     * @param subject 主题
     * @return 令牌
     */
    private static String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * 从令牌中获取用户 ID
     *
     * @param token 令牌
     * @return 用户 ID
     */
    public static Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return Long.valueOf(claims.get("userId").toString());
    }

    /**
     * 验证令牌是否有效
     *
     * @param token 令牌
     * @return 是否有效
     */
    public static Boolean validateToken(String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            final Date expiration = claims.getExpiration();
            return !expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从令牌中获取声明
     *
     * @param token 令牌
     * @return 声明
     */
    private static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 设置密钥
     *
     * @param secret 密钥
     */
    public static void setSecret(String secret) {
        JwtUtil.SECRET = secret;
    }

    /**
     * 设置过期时间
     *
     * @param expiration 过期时间 (毫秒)
     */
    public static void setExpiration(long expiration) {
        JwtUtil.EXPIRATION = expiration;
    }
}
