package com.ex.shop.admin.order.service;

import com.ex.shop.admin.order.model.AdminOrder;
import com.ex.shop.admin.order.repository.AdminOrderRepository;
import com.ex.shop.common.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminExportService { // wstrzykuję do kontrolera

    private final AdminOrderRepository orderRepository;

    public List<AdminOrder> exportOrders(LocalDateTime from, LocalDateTime to, OrderStatus orderStatus) {
        // 18.0 metoda będzie pobierała zamówienia w zakresie dat od from do to, filtrowane po orderStatus:
        return orderRepository.findAllByPlaceDateIsBetweenAndOrderStatus(from, to, orderStatus);
    }
}