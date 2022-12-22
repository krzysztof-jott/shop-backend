package com.ex.shop.product.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewDto {
    private final Long id;
    private final Long productId;
    private final String authorName;
    private final String content;
    private final boolean moderated;
}