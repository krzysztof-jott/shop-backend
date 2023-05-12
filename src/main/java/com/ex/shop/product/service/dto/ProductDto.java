package com.ex.shop.product.service.dto;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class ProductDto {
    private final Long id;
    private final String name;
    private final Long categoryId;
    private final String description;
    private final String fullDescription;
    private final BigDecimal price;
    private final BigDecimal salePrice;
    private final String currency;
    private final String image;
    private final String slug;
    private final List<ReviewDto> reviews;
}