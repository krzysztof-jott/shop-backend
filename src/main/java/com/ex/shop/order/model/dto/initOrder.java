package com.ex.shop.order.model.dto;

import com.ex.shop.order.model.Payment;
import com.ex.shop.order.model.Shipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

// 15.1 DTO:
@Getter
@Builder
@AllArgsConstructor
public class initOrder {
    private List<Shipment> shipments;
    // 24.0 dodaję payment
    private List<Payment> payments;
}