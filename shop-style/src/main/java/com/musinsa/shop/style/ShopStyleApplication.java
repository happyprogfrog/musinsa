package com.musinsa.shop.style;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = "com.musinsa.shop.domain.entity")
@SpringBootApplication
public class ShopStyleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopStyleApplication.class, args);
    }

}
