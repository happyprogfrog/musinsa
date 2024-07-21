package com.musinsa.shop.style.common.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.musinsa.shop.style.controller.consts.ErrorCodes;
import jakarta.annotation.Nullable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(value = NON_NULL)
public record ApiResponse<T>(
        boolean isSuccess,
        @Nullable
        T data,
        @Nullable
        ApiErrorResponse error
) {
    public ApiResponse(boolean isSuccess, T data, ApiErrorResponse error) {
        this.isSuccess = isSuccess;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> just(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> fromErrorCodes(ErrorCodes errorCodes) {
        final ApiErrorResponse errorResponse = new ApiErrorResponse(errorCodes.message, errorCodes.code);
        return new ApiResponse<>(false, null, errorResponse);
    }
}
