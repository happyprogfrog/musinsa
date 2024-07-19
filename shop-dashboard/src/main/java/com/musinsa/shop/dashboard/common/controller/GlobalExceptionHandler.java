package com.musinsa.shop.dashboard.common.controller;

import com.musinsa.shop.dashboard.controller.exception.CommonShopDashboardHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
}
