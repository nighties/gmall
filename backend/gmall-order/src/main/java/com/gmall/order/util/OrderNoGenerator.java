package com.gmall.order.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * 订单号生成器
 */
public class OrderNoGenerator {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    private static final Random random = new Random();

    /**
     * 生成订单号
     * 格式：yyyyMMddHHmmss + 6 位随机数
     *
     * @return 订单号
     */
    public static String generate() {
        String timestamp = LocalDateTime.now().format(formatter);
        String randomStr = String.format("%06d", random.nextInt(1000000));
        return timestamp + randomStr;
    }
}
