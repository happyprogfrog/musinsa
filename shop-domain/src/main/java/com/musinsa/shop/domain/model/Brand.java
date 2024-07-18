package com.musinsa.shop.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Brand {
    private Long id;
    private String alias;
    private String name;

    public void update(String alias, String name) {
        this.alias = alias;
        this.name = name;
    }
}
