package com.ex.shop.category.dto;

import com.ex.shop.common.dto.ProductListDto;
import com.ex.shop.common.model.Category;
import org.springframework.data.domain.Page;

public record CategoryProductsDto(Category category, Page<ProductListDto> products) {
}