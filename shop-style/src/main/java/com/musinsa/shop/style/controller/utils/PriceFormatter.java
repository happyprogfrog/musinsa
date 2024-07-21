package com.musinsa.shop.style.controller.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class PriceFormatter {
    public static String convertToKorFormat(Long price) {
        return NumberFormat.getNumberInstance(Locale.KOREA).format(price);
    }
}
