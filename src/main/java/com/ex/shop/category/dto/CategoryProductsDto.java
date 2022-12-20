package com.ex.shop.category.dto;

import com.ex.shop.common.dto.ProductListDto;
import com.ex.shop.common.model.Category;
import org.springframework.data.domain.Page;

// 39.0 tworzę DTO
//49.9 zmieniam Product na <ProductListDto>
public record CategoryProductsDto(Category category, Page<ProductListDto> products) {
}