package com.banmingi.bmshop.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @auther 半命i 2019/10/18
 * @description
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ItemServicelication {

    public static void main(String[] args) {
        SpringApplication.run(ItemServicelication.class);
    }

}
