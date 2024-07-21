package com.musinsa.shop.dashboard.controller.exception;

import com.musinsa.shop.dashboard.controller.consts.ErrorCodes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class CommonShopDashboardHttpException extends RuntimeException {
    private final ErrorCodes errorCodes;
    private final HttpStatus httpStatus;
}
