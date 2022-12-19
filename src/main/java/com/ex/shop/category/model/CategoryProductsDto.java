package com.ex.shop.category.model;

import com.ex.shop.product.model.Product;
import org.springframework.data.domain.Page;

// 39.0 tworzę DTO
public record CategoryProductsDto(Category category, Page<Product> products) {
}