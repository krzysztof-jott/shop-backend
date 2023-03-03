package com.ex.shop.order.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderListDto { // 51.0

    private Long id;
    private LocalDateTime placeDate;
    // 56.1 zmieniam OrderStatus na String
    private String orderStatus;
    private BigDecimal grossValue;
}