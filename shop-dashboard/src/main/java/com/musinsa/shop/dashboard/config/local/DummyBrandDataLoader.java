package com.musinsa.shop.dashboard.config.local;

import com.musinsa.shop.dashboard.repository.jpa.BrandJpaRepository;
import com.musinsa.shop.domain.entity.BrandJpaEntity;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
@Order(2)
public class DummyBrandDataLoader implements CommandLineRunner {
    private final BrandJpaRepository brandJpaRepository;

    public DummyBrandDataLoader(BrandJpaRepository brandJpaRepository) {
        this.brandJpaRepository = brandJpaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 브랜드 더미 데이터 생성
        for (char ch = 'A'; ch <= 'I'; ch++) {
            BrandJpaEntity brand = new BrandJpaEntity(
                    "brand" + Character.toLowerCase(ch),
                    Character.toString(ch));
            brandJpaRepository.save(brand);
        }
    }
}
