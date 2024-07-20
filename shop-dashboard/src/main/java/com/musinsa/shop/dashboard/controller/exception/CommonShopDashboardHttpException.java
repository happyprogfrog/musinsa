package com.musinsa.shop.dashboard.controller.exception;

import com.musinsa.shop.dashboard.controller.consts.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class CommonShopDashboardHttpException extends RuntimeException {
    private final ErrorCodes errorCodes;
    private final HttpStatus httpStatus;
}
