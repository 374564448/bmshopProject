package com.banmingi.bmshop.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @auther 半命i 2019/11/18
 * @description
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.banmingi.bmshop.user.mapper")
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class);
    }
}
