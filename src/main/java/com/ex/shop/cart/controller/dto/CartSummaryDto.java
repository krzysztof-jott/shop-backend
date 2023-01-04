package com.ex.shop.cart.controller.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CartSummaryDto { // główne DTO
    private Long id;
    private List<CartSummaryItemDto> items;
    private SummaryDto summary;
}