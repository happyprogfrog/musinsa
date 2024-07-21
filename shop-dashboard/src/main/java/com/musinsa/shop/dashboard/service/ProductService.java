package com.musinsa.shop.dashboard.service;

import com.musinsa.shop.dashboard.controller.dto.ProductDto.CreateProductRequest;
import com.musinsa.shop.dashboard.controller.dto.ProductDto.UpdateProductRequest;
import com.musinsa.shop.dashboard.service.exception.BrandNotFoundException;
import com.musinsa.shop.dashboard.service.exception.CategoryNotFoundException;
import com.musinsa.shop.dashboard.service.exception.InvalidPriceException;
import com.musinsa.shop.dashboard.service.exception.ProductNotFoundException;
import com.musinsa.shop.dashboard.service.persistence.CommandProductPort;
import com.musinsa.shop.dashboard.service.persistence.LoadBrandPort;
import com.musinsa.shop.dashboard.service.persistence.LoadCategoryPort;
import com.musinsa.shop.dashboard.service.persistence.LoadProductPort;
import com.musinsa.shop.domain.enums.CategoryCode;
import com.musinsa.shop.domain.model.Brand;
import com.musinsa.shop.domain.model.Category;
import com.musinsa.shop.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductService {
    private final LoadBrandPort loadBrandPort;
    private final LoadCategoryPort loadCategoryPort;
    private final LoadProductPort loadProductPort;
    private final CommandProductPort commandProductPort;

    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        return findProductByIdOrThrow(productId);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByBrandAlias(String brandAlias) {
        findBrandByAliasOrThrow(brandAlias);
        return loadProductPort.findProductsByBrandAlias(brandAlias);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByCategoryCode(String categoryCodeKey) {
        CategoryCode categoryCode = getCategoryCodeFromKey(categoryCodeKey);
        findCategoryByCodeOrThrow(categoryCode);

        return loadProductPort.findProductsByCategoryCode(categoryCode);
    }

    public Product createProduct(CreateProductRequest request) {
        validateProductRequest(request.name(), request.price());

        Brand brand = findBrandByIdOrThrow(request.brandId());

        CategoryCode categoryCode = getCategoryCodeFromKey(request.categoryCodeKey());
        Category category = findCategoryByCodeOrThrow(categoryCode);

        Product product = Product.builder()
                .brand(brand)
                .category(category)
                .name(request.name())
                .price(request.price())
                .build();

        return commandProductPort.createProduct(product);
    }

    public Product modifyProduct(UpdateProductRequest request) {
        Product product = findProductByIdOrThrow(request.id());

        validateProductRequest(request.name(), request.price());

        Brand brand = findBrandByIdOrThrow(request.brandId());

        CategoryCode categoryCode = getCategoryCodeFromKey(request.categoryCodeKey());
        Category category = findCategoryByCodeOrThrow(categoryCode);

        product.update(brand, category, request.name(), request.price());

        return commandProductPort.modifyProduct(product);
    }

    public void deleteProduct(Long productId) {
        findProductByIdOrThrow(productId);

        commandProductPort.deleteProduct(productId);
    }

    private void validateProductRequest(String name, Long price) {
        Assert.hasLength(name, "제품명은 비워둘 수 없습니다");
        validatePrice(price);
    }

    private void validatePrice(Long price) {
        if (price < 0) {
            throw new InvalidPriceException();
        }
    }

    private Brand findBrandByIdOrThrow(Long brandId) {
        return loadBrandPort.findBrandById(brandId)
                .orElseThrow(BrandNotFoundException::new);
    }

    private Brand findBrandByAliasOrThrow(String brandAlias) {
        return loadBrandPort.findBrandByAlias(brandAlias)
                .orElseThrow(BrandNotFoundException::new);
    }

    private Product findProductByIdOrThrow(Long productId) {
        return loadProductPort.findProductById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    private Category findCategoryByCodeOrThrow(CategoryCode categoryCode) {
        return loadCategoryPort.findCategoryByCode(categoryCode)
                .orElseThrow(CategoryNotFoundException::new);
    }

    private CategoryCode getCategoryCodeFromKey(String categoryCodeKey) {
        CategoryCode categoryCode;

        try {
            categoryCode = CategoryCode.fromKey(categoryCodeKey);
        } catch (IllegalArgumentException ex) {
            throw new CategoryNotFoundException();
        }

        return categoryCode;
    }
}
