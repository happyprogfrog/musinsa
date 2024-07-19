package com.musinsa.shop.dashboard.config.local;

import com.musinsa.shop.dashboard.repository.jpa.BrandJpaRepository;
import com.musinsa.shop.dashboard.repository.jpa.entity.BrandJpaEntity;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class DummyBrandDataLoader {

    @Bean
    public ApplicationRunner loadData(BrandJpaRepository brandJpaRepository) {
        return args -> {
            // 브랜드 더미데이터 생성
            for (char ch = 'A'; ch < 'I'; ch++) {
                BrandJpaEntity brand = new BrandJpaEntity(
                        "brand" + Character.toLowerCase(ch),
                        "브랜드 " + ch);
                brandJpaRepository.save(brand);
            }
        };
    }
}
