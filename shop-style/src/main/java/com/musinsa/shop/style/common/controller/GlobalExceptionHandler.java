package com.musinsa.shop.style.common.controller;

import com.musinsa.shop.style.controller.consts.ErrorCodes;
import com.musinsa.shop.style.controller.exception.CommonShopStyleHttpException;
import com.musinsa.shop.style.service.exception.BrandNotFoundException;
import com.musinsa.shop.style.service.exception.CategoryNotFoundException;
import com.musinsa.shop.style.service.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.charset.StandardCharsets;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {CommonShopStyleHttpException.class})
    public ResponseEntity<ApiResponse<Void>> handleCommonShopStyleHttpException(
            CommonShopStyleHttpException ex
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
        return handleCommonShopStyleHttpException(new CommonShopStyleHttpException(ErrorCodes.INVALID_REQUEST, HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(BrandNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleBrandNotFoundException(BrandNotFoundException ex) {
        return handleCommonShopStyleHttpException(new CommonShopStyleHttpException(ErrorCodes.BRAND_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        return handleCommonShopStyleHttpException(new CommonShopStyleHttpException(ErrorCodes.CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleProductNotFoundException(ProductNotFoundException ex) {
        return handleCommonShopStyleHttpException(new CommonShopStyleHttpException(ErrorCodes.PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND));
    }
}
