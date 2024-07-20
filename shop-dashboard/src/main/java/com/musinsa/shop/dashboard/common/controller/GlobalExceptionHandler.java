package com.musinsa.shop.dashboard.common.controller;

import com.musinsa.shop.dashboard.controller.consts.ErrorCodes;
import com.musinsa.shop.dashboard.controller.exception.CommonShopDashboardHttpException;
import com.musinsa.shop.dashboard.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.StandardCharsets;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {CommonShopDashboardHttpException.class})
    public ResponseEntity<ApiResponse<Void>> handleCommonShopDashboardHttpException(
            CommonShopDashboardHttpException ex
    ) {
        final ApiResponse<Void> body = ApiResponse.fromErrorCodes(ex.getErrorCodes());
        final MediaType contentType = new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8);
        final HttpStatus status = ex.getHttpStatus();

        return ResponseEntity
                .status(status)
                .contentType(contentType)
                .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return handleCommonShopDashboardHttpException(new CommonShopDashboardHttpException(ErrorCodes.INVALID_REQUEST, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleBrandNotFoundException(BrandNotFoundException ex) {
        return handleCommonShopDashboardHttpException(new CommonShopDashboardHttpException(ErrorCodes.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(DuplicateAliasException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicateAliasException(DuplicateAliasException ex) {
        return handleCommonShopDashboardHttpException(new CommonShopDashboardHttpException(ErrorCodes.DUPLICATE_ALIAS, HttpStatus.CONFLICT));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return handleCommonShopDashboardHttpException(new CommonShopDashboardHttpException(ErrorCodes.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductNotFoundException(ProductNotFoundException ex) {
        return handleCommonShopDashboardHttpException(new CommonShopDashboardHttpException(ErrorCodes.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(InvalidPriceException.class)
    public ResponseEntity<ApiResponse<Void>> handleInvalidPriceException(InvalidPriceException ex) {
        return handleCommonShopDashboardHttpException(new CommonShopDashboardHttpException(ErrorCodes.INVALID_PRICE, HttpStatus.BAD_REQUEST));
    }
}
