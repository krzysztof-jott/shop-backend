package com.ex.shop.order.controller;

import com.ex.shop.order.model.dto.OrderDto;
import com.ex.shop.order.model.dto.OrderSummary;
import com.ex.shop.order.model.dto.initOrder;
import com.ex.shop.order.service.OrderService;
import com.ex.shop.order.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ShipmentService shipmentService;

    @PostMapping
    public OrderSummary placeOrder(@RequestBody OrderDto orderDto) {
        return orderService.placeOrder(orderDto);
    }

    @GetMapping("/initData")
    public initOrder initData() { // 15.0 towrzę DTO w modelu
        // 16.1 dokańczam
        return initOrder.builder()
                .shipments(shipmentService.getShipments())
                .build();
    }
}