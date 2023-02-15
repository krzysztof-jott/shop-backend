package com.ex.shop.admin.order.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

// 26.0 nowe DTO:
@Getter
@Builder
public class AdminOrderStats {

    private List<Integer> label;
    private List<BigDecimal> sale;
    private List<Long> order; // tworzę do tego osobny serwis
    private Long ordersCount;
    private BigDecimal salesSum;
}