package com.musinsa.shop.dashboard.controller;

import com.musinsa.shop.dashboard.common.controller.ApiResponse;
import com.musinsa.shop.dashboard.controller.dto.ProductDto.CreateProductRequest;
import com.musinsa.shop.dashboard.controller.dto.ProductDto.ProductResponse;
import com.musinsa.shop.dashboard.service.ProductService;
import com.musinsa.shop.domain.model.Product;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.musinsa.shop.dashboard.controller.dto.ProductDto.UpdateProductRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @GetMapping("/{productId}")
    ApiResponse<ProductResponse> getProductById(@PathVariable(name = "productId") Long productId) {
        Product product = productService.getProductById(productId);

        return ApiResponse.just(ProductResponse.of(product));
    }

    @GetMapping(params = "brandAlias")
    ApiResponse<List<ProductResponse>> getAllBrandsByBrandAlias(@RequestParam(name = "brandAlias") String brandAlias) {
        List<ProductResponse> productResponses = productService.getProductsByBrandAlias(brandAlias).stream()
                .map(ProductResponse::of)
                .toList();

        return ApiResponse.just(productResponses);
    }

    @GetMapping(params = "categoryCode")
    ApiResponse<List<ProductResponse>> getAllBrandsByCategoryCode(@RequestParam(name = "categoryCode") String categoryCodeKey) {
        List<ProductResponse> productResponses = productService.getProductsByCategoryCode(categoryCodeKey).stream()
                .map(ProductResponse::of)
                .toList();

        return ApiResponse.just(productResponses);
    }

    @PostMapping
    ApiResponse<ProductResponse> postProduct(@Valid @RequestBody CreateProductRequest request) {
        Product product = productService.createProduct(request);

        return ApiResponse.just(ProductResponse.of(product));
    }

    @PutMapping
    ApiResponse<ProductResponse> putProduct(@Valid @RequestBody UpdateProductRequest request) {
        Product product = productService.modifyProduct(request);

        return ApiResponse.just(ProductResponse.of(product));
    }

    @DeleteMapping("/{productId}")
    void deleteProduct(@PathVariable(name = "productId") Long productId) {
        productService.deleteProduct(productId);
    }
}
