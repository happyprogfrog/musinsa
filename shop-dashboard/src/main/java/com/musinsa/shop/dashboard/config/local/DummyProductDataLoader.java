package com.musinsa.shop.dashboard.config.local;

import com.musinsa.shop.dashboard.repository.jpa.BrandJpaRepository;
import com.musinsa.shop.dashboard.repository.jpa.CategoryJpaRepository;
import com.musinsa.shop.dashboard.repository.jpa.ProductJpaRepository;
import com.musinsa.shop.dashboard.repository.jpa.entity.BrandJpaEntity;
import com.musinsa.shop.dashboard.repository.jpa.entity.CategoryJpaEntity;
import com.musinsa.shop.dashboard.repository.jpa.entity.ProductJpaEntity;
import com.musinsa.shop.domain.enums.CategoryCode;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Profile("local")
@Order(3)
public class DummyProductDataLoader implements CommandLineRunner {
    private final ProductJpaRepository productJpaRepository;
    private final BrandJpaRepository brandJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    public DummyProductDataLoader(ProductJpaRepository productJpaRepository,
                                  BrandJpaRepository brandJpaRepository,
                                  CategoryJpaRepository categoryJpaRepository) {
        this.productJpaRepository = productJpaRepository;
        this.brandJpaRepository = brandJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 가격 데이터 매핑
        Map<String, Long[]> priceMap = createPriceMap();

        // 카테고리 코드 배열
        CategoryCode[] categoryCodes = {
                CategoryCode.TOP,
                CategoryCode.OUTER,
                CategoryCode.PANTS,
                CategoryCode.SNEAKERS,
                CategoryCode.HAT,
                CategoryCode.SOCKS,
                CategoryCode.ACCESSORY
        };

        // 제품 더미 데이터 생성
        priceMap.forEach((brandAlias, prices) -> {
            Optional<BrandJpaEntity> brand = brandJpaRepository.findByAlias(brandAlias);
            if (brand.isPresent()) {
                for (int i = 0; i < categoryCodes.length; i++) {
                    CategoryCode code = categoryCodes[i];
                    Optional<CategoryJpaEntity> category = categoryJpaRepository.findByCode(code);
                    if (category.isPresent()) {
                        Long price = prices[i];
                        ProductJpaEntity productJpaEntity = createProductEntity(brand.get(), category.get(), price, i + 1);
                        productJpaRepository.save(productJpaEntity);
                    }
                }
            }
        });
    }

    private Map<String, Long[]> createPriceMap() {
        Map<String, Long[]> priceMap = new HashMap<>();
        priceMap.put("branda", new Long[]{11200L, 5500L, 4200L, 9000L, 2000L, 1700L, 1800L, 2300L});
        priceMap.put("brandb", new Long[]{10500L, 5900L, 3800L, 9100L, 2100L, 2000L, 2000L, 2200L});
        priceMap.put("brandc", new Long[]{10000L, 6200L, 3300L, 9200L, 2200L, 1900L, 2200L, 2100L});
        priceMap.put("brandd", new Long[]{10100L, 5100L, 3000L, 9500L, 2500L, 1500L, 2400L, 2000L});
        priceMap.put("brande", new Long[]{10700L, 5000L, 3800L, 9900L, 2300L, 1800L, 2100L, 2100L});
        priceMap.put("brandf", new Long[]{11200L, 7200L, 4000L, 9300L, 2100L, 1600L, 2300L, 1900L});
        priceMap.put("brandg", new Long[]{10500L, 5800L, 3900L, 9000L, 2200L, 1700L, 2100L, 2000L});
        priceMap.put("brandh", new Long[]{10800L, 6300L, 3100L, 9700L, 2100L, 1600L, 2000L, 2000L});
        priceMap.put("brandi", new Long[]{11400L, 6700L, 3200L, 9500L, 2400L, 1700L, 1700L, 2400L});
        return priceMap;
    }

    private ProductJpaEntity createProductEntity(BrandJpaEntity brand, CategoryJpaEntity category, Long price, int index) {
        return new ProductJpaEntity(
                brand,
                category,
                brand.getName() + "의 제품 " + index,
                price
        );
    }
}
