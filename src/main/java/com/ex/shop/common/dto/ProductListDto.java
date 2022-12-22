package com.ex.shop.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ProductListDto {
    private final Long id;
    private final String name;
    private final String description;
    private final BigDecimal price;
    private final String currency;
    private final String image;
    private final String slug;
}