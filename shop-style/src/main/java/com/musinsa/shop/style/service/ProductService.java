package com.musinsa.shop.style.service;

import com.musinsa.shop.domain.enums.CategoryCode;
import com.musinsa.shop.domain.model.Brand;
import com.musinsa.shop.domain.model.Category;
import com.musinsa.shop.domain.model.Product;
import com.musinsa.shop.style.service.dto.ProductLowestPriceListDto;
import com.musinsa.shop.style.service.dto.ProductPriceDto;
import com.musinsa.shop.style.service.dto.ProductPriceListDto;
import com.musinsa.shop.style.service.exception.ProductNotFoundException;
import com.musinsa.shop.style.service.persistence.LoadBrandPort;
import com.musinsa.shop.style.service.persistence.LoadCategoryPort;
import com.musinsa.shop.style.service.persistence.LoadProductPort;
import com.musinsa.shop.style.service.utils.CategoryCodeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {
    private final LoadCategoryPort loadCategoryPort;
    private final LoadProductPort loadProductPort;
    private final LoadBrandPort loadBrandPort;

    @Transactional(readOnly = true)
    public ProductPriceListDto getLowestPriceBrandInfoByCategory() {
        List<ProductPriceDto> minPriceProducts = new ArrayList<>();
        long totalPrice = 0L;

        for (Category category : loadCategoryPort.findAllCategories()) {
            List<Product> products = loadProductPort.findAllByCategoryCodeOrderByPriceAsc(category.getCode());
            if (!products.isEmpty()) {
                Product lowestPriceProduct = products.stream()
                        .min(Comparator.comparing(Product::getPrice)
                                .thenComparing(p -> p.getBrand().getName(), Comparator.reverseOrder()))
                        .orElse(null);

                if (lowestPriceProduct != null) {
                    minPriceProducts.add(new ProductPriceDto(
                            lowestPriceProduct.getBrand(),
                            category.getCode(),
                            lowestPriceProduct.getName(),
                            lowestPriceProduct.getPrice()
                    ));

                    totalPrice += lowestPriceProduct.getPrice();
                }
            }
        }

        return new ProductPriceListDto(minPriceProducts, totalPrice);
    }

    @Transactional(readOnly = true)
    public ProductLowestPriceListDto getLowestTotalPriceInfoByBrand() {
        int numOfCategories = loadCategoryPort.findAllCategories().size();

        Brand minPriceBrand = null;
        List<ProductPriceDto> minPriceProducts = null;
        long minPriceSum = Long.MAX_VALUE;

        for (Brand brand : loadBrandPort.findAllBrands()) {
            List<Product> products = loadProductPort.findAllByBrandIdOrderByCategoryCodeAscAndPriceAsc(brand.getId());
            Map<Category, List<Product>> productsByCategory = products.stream()
                    .collect(Collectors.groupingBy(Product::getCategory));

            if (productsByCategory.size() == numOfCategories) {
                List<ProductPriceDto> currentMinPriceProducts = productsByCategory.values().stream()
                        .map(categoryProducts -> ProductPriceDto.fromDomain(categoryProducts.get(0)))
                        .sorted(Comparator.comparing(ProductPriceDto::categoryCode))
                        .toList();

                long priceSum = currentMinPriceProducts.stream()
                        .mapToLong(ProductPriceDto::price)
                        .sum();

                if (priceSum < minPriceSum) {
                    minPriceSum = priceSum;
                    minPriceBrand = brand;
                    minPriceProducts = currentMinPriceProducts;
                }
            }
        }

        return new ProductLowestPriceListDto(minPriceBrand, minPriceProducts, minPriceSum);
    }

    @Transactional(readOnly = true)
    public ProductPriceDto getLowestPriceInfoByCategoryCode(String categoryCodeKey) {
        CategoryCode categoryCode = CategoryCodeUtils.getCategoryCodeFromKey(categoryCodeKey);
        Product product = loadProductPort.findLowestPriceByCategoryCode(categoryCode)
                .orElseThrow(ProductNotFoundException::new);

        return ProductPriceDto.fromDomain(product);
    }

    @Transactional(readOnly = true)
    public ProductPriceDto getHighestPriceInfoByCategoryCode(String categoryCodeKey) {
        CategoryCode categoryCode = CategoryCodeUtils.getCategoryCodeFromKey(categoryCodeKey);
        Product product = loadProductPort.findHighestPriceByCategoryCode(categoryCode)
                .orElseThrow(ProductNotFoundException::new);

        return ProductPriceDto.fromDomain(product);
    }
}
