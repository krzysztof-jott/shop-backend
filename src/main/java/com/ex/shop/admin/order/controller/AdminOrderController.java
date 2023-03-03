package com.ex.shop.admin.order.controller;

import com.ex.shop.admin.order.controller.dto.AdminInitDataDto;
import com.ex.shop.admin.order.controller.dto.AdminOrderDto;
import com.ex.shop.admin.order.controller.mapper.AdminOrderMapper;
import com.ex.shop.admin.order.model.AdminOrder;
import com.ex.shop.admin.order.service.AdminOrderService;
import com.ex.shop.common.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping
    public Page<AdminOrderDto> getOrders(Pageable pageable) {
        return AdminOrderMapper.mapToPageDtos(adminOrderService.getOrders(pageable));
    }

    @GetMapping("/{id}")
    public AdminOrder getOrder(@PathVariable Long id) {
        return adminOrderService.getOrder(id);
    }

    //    2.1
    @PatchMapping("/{id}")
    public void patchOrder(@PathVariable Long id, @RequestBody Map<String, String> values) {
        adminOrderService.patchOrder(id, values); // idę do serwisu
    }

    // 3.0
    @GetMapping("/initData")
    public AdminInitDataDto getInitData() {
        // 3.1 robię mapę ze statusami:
        return new AdminInitDataDto(createOrderStatusesMap());
    }

    private Map<String, String> createOrderStatusesMap() {
        // 3.2 potrzebuję mapy, która będzie zawierała statusy:
        HashMap<String, String> statuses = new HashMap<>();
        // 4.3 wypełniam mapę statusami:
        for (OrderStatus value : OrderStatus.values()) {
            statuses.put(value.name(), value.getValue());
        }
        return statuses;
    }
}