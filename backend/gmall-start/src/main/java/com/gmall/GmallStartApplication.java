package com.gmall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 美团小程序后台管理系统启动类
 */
@SpringBootApplication
@MapperScan("com.gmall.*.mapper")
public class GmallStartApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallStartApplication.class, args);
        System.out.println("====================================");
        System.out.println("    美团小程序后台启动成功!");
        System.out.println("    API 文档：http://localhost:8080/doc.html");
        System.out.println("====================================");
    }
}
