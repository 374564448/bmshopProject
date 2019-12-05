package com.banmingi.bmshop.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @auther 半命i 2019/11/29
 * @description
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CartServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CartServiceApplication.class);
    }
}
