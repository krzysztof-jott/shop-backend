package com.ex.shop.admin.order.controller;

import com.ex.shop.admin.order.model.dto.AdminOrderStats;
import com.ex.shop.admin.order.service.AdminOrderStatsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/orders/stats")
@AllArgsConstructor
public class AdminOrderStatsController {

    private final AdminOrderStatsService adminOrderStatsService;

    @GetMapping
    public AdminOrderStats getOrderStatistics() { // tworzę DTO, w modelu, bo będę je zwracać w serwisie
        // 26.2 tworzę metodę getStatistics():
        return adminOrderStatsService.getStatistics();
    }
}