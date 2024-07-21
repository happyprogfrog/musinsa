package com.musinsa.shop.style.controller.exception;

import com.musinsa.shop.style.controller.consts.ErrorCodes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class CommonShopStyleHttpException extends RuntimeException {
    private final ErrorCodes errorCodes;
    private final HttpStatus httpStatus;
}
