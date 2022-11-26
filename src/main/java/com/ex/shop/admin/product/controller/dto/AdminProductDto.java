package com.ex.shop.admin.product.controller.dto;

import lombok.Getter;

import java.math.BigDecimal;

// 16.6 bez pola id!
@Getter
public class AdminProductDto {

    private String name;
    private String category;
    private String description;
    private BigDecimal price;
    private String currency;
}