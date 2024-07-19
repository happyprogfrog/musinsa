package com.musinsa.shop.dashboard.common.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.musinsa.shop.dashboard.controller.consts.ErrorCodes;
import jakarta.annotation.Nullable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@JsonInclude(value = NON_NULL)
public record ApiResponse<T>(
        @Nullable
        T data,
        @Nullable
        ApiErrorResponse error
) {
    public static <T> ApiResponse<T> just(T data) {
        return new ApiResponse<>(data, null);
    }

    public static <T> ApiResponse<T> fromErrorCodes(ErrorCodes errorCodes) {
        final ApiErrorResponse errorResponse = new ApiErrorResponse(errorCodes.message, errorCodes.code);
        return new ApiResponse<>(null, errorResponse);
    }
}
