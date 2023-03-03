package com.ex.shop.order.controller;

import com.ex.shop.order.model.dto.OrderDto;
import com.ex.shop.order.model.dto.OrderListDto;
import com.ex.shop.order.model.dto.OrderSummary;
import com.ex.shop.order.model.dto.initOrder;
import com.ex.shop.order.service.OrderService;
import com.ex.shop.order.service.PaymentService;
import com.ex.shop.order.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final ShipmentService shipmentService;
    private final PaymentService paymentService;

    @PostMapping
    public OrderSummary placeOrder(@RequestBody OrderDto orderDto, @AuthenticationPrincipal Long userId) {
        return orderService.placeOrder(orderDto, userId);
    }

    @GetMapping("/initData")
    // 47.0 dodaję adnotację AuthenticationPrincipal
    // 48.1 teraz zmieniam String na Long i name na userId:
    // 48.2 przenoszę teraz całą adnotację do metody placeOrder
    public initOrder initData() { // 15.0 towrzę DTO w modelu
        // 16.1 dokańczam
        return initOrder.builder()
                .shipments(shipmentService.getShipments())
                // 25.0 dodaję payment i tworzę metodę i dodaję do serwisu:
                .payments(paymentService.getPayments())
                .build();
    }

    // 50.0 usługa do pobierania zamówień:
    @GetMapping
    public List<OrderListDto> getOrders(@AuthenticationPrincipal Long userId) {
        // 58.1 dodaję wyjątek, żeby nie wyswietlały się wszystkie zamówienia nieprzypisane do użytkowników:
        if (userId == null) {
            throw new IllegalArgumentException("Brak użytkownika");
        }
        // 50.1 dorabiam metodę serwisową, która te zamówienia będzie zwracała:
        // 51.1 dodaję metodę prywatną mapTo...:
        return orderService.getOrdersForCustomer(userId);
    }
}