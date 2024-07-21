package com.musinsa.shop.dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = "com.musinsa.shop.domain.entity")
@SpringBootApplication
public class ShopDashboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopDashboardApplication.class, args);
    }

}
