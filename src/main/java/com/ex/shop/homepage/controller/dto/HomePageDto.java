package com.ex.shop.homepage.controller.dto;

import com.ex.shop.common.model.Product;
import java.util.List;

public record HomePageDto(List<Product> saleProducts) {
}