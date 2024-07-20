package com.musinsa.shop.dashboard.controller;

import com.musinsa.shop.dashboard.common.controller.ApiResponse;
import com.musinsa.shop.dashboard.controller.dto.BrandDto.BrandResponse;
import com.musinsa.shop.dashboard.controller.dto.BrandDto.CreateBrandRequest;
import com.musinsa.shop.dashboard.service.BrandService;
import com.musinsa.shop.domain.model.Brand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.musinsa.shop.dashboard.controller.dto.BrandDto.UpdateBrandRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/brands")
public class BrandController {
    private final BrandService brandService;

    @GetMapping("/{brandId}")
    ApiResponse<BrandResponse> getBrandById(@PathVariable(name = "brandId") Long brandId) {
        Brand brand = brandService.getBrandById(brandId);

        return ApiResponse.just(BrandResponse.fromDomain(brand));
    }

    @GetMapping
    ApiResponse<List<BrandResponse>> getAllBrands() {
        List<BrandResponse> brandResponses = brandService.getAllBrands().stream()
                .map(BrandResponse::fromDomain)
                .toList();

        return ApiResponse.just(brandResponses);
    }

    @PostMapping
    ApiResponse<BrandResponse> postBrand(@Valid @RequestBody CreateBrandRequest request) {
        Brand brand = brandService.createBrand(request);

        return ApiResponse.just(BrandResponse.fromDomain(brand));
    }

    @PutMapping
    ApiResponse<BrandResponse> putBrand(@Valid @RequestBody UpdateBrandRequest request) {
        Brand brand = brandService.modifyBrand(request);

        return ApiResponse.just(BrandResponse.fromDomain(brand));
    }

    @DeleteMapping("/{brandId}")
    void deleteBrand(@PathVariable(name = "brandId") Long brandId) {
        brandService.deleteBrand(brandId);
    }
}
