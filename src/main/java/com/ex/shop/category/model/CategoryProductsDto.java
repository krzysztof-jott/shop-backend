package com.ex.shop.category.model;

import com.ex.shop.product.controller.dto.ProductListDto;
import org.springframework.data.domain.Page;

// 39.0 tworzę DTO
//49.9 zmieniam Product na <ProductListDto>
public record CategoryProductsDto(Category category, Page<ProductListDto> products) {
}