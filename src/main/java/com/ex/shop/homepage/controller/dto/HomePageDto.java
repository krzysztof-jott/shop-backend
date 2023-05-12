package com.ex.shop.homepage.controller.dto;

import com.ex.shop.common.model.Product;
import java.util.List;

// tylko lista promocyjnych produktów
public record HomePageDto(List<Product> saleProducts) {
}