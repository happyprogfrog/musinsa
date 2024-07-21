package com.musinsa.shop.style.common.controller;

import jakarta.validation.constraints.NotNull;

public record ApiErrorResponse(
        @NotNull
        String localMessage,
        @NotNull
        String code
) {}
