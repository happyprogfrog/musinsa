package com.musinsa.shop.dashboard.controller;

import com.musinsa.shop.dashboard.common.controller.ApiResponse;
import com.musinsa.shop.dashboard.controller.consts.ErrorCodes;
import com.musinsa.shop.dashboard.controller.dto.BrandDto.BrandResponse;
import com.musinsa.shop.dashboard.controller.dto.BrandDto.CreateBrandRequest;
import com.musinsa.shop.dashboard.controller.exception.CommonShopDashboardHttpException;
import com.musinsa.shop.dashboard.service.BrandService;
import com.musinsa.shop.dashboard.service.exception.BrandNotFoundException;
import com.musinsa.shop.dashboard.service.exception.DuplicateAliasException;
import com.musinsa.shop.domain.model.Brand;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.musinsa.shop.dashboard.controller.dto.BrandDto.UpdateBrandRequest;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/{brandId}")
    ApiResponse<BrandResponse> getBrandById(@PathVariable(name = "brandId") Long brandId) {
        Brand brand;

        try {
            brand = brandService.getBrandById(brandId);
        } catch (BrandNotFoundException ex) {
            throw new CommonShopDashboardHttpException(ErrorCodes.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        return ApiResponse.just(BrandResponse.of(brand));
    }

    @GetMapping
    ApiResponse<List<BrandResponse>> getAllBrands() {
        List<BrandResponse> brandResponses = brandService.getAllBrands().stream()
                .map(BrandResponse::of)
                .toList();

        return ApiResponse.just(brandResponses);
    }

    @PostMapping
    ApiResponse<BrandResponse> postBrand(@Valid @RequestBody CreateBrandRequest request) {
        Brand brand;

        try {
            brand = brandService.createBrand(request);
        } catch (DuplicateAliasException ex) {
            throw new CommonShopDashboardHttpException(ErrorCodes.DUPLICATE_ALIAS, HttpStatus.CONFLICT);
        }

        return ApiResponse.just(BrandResponse.of(brand));
    }

    @PutMapping
    ApiResponse<BrandResponse> putBrand(@Valid @RequestBody UpdateBrandRequest request) {
        Brand brand;

        try {
            brand = brandService.modifyBrand(request);
        } catch (BrandNotFoundException ex) {
            throw new CommonShopDashboardHttpException(ErrorCodes.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        return ApiResponse.just(BrandResponse.of(brand));
    }

    @DeleteMapping("/{brandId}")
    void deleteBrand(@PathVariable(name = "brandId") Long brandId) {
        try {
            brandService.deleteBrand(brandId);
        } catch (BrandNotFoundException ex) {
            throw new CommonShopDashboardHttpException(ErrorCodes.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }
}
