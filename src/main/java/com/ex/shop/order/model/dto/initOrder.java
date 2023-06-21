package com.ex.shop.order.model.dto;

import com.ex.shop.order.model.Payment;
import com.ex.shop.order.model.Shipment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class initOrder {

    private List<Shipment> shipments;
    private List<Payment> payments;
}