package com.ex.shop.admin.order.service;

import com.ex.shop.admin.order.model.AdminOrder;
import com.ex.shop.admin.order.model.dto.AdminOrderStats;
import com.ex.shop.admin.order.repository.AdminOrderRepository;
import com.ex.shop.common.model.OrderStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.IntStream;
import static java.util.stream.Collectors.toMap;

@Service
@AllArgsConstructor
public class AdminOrderStatsService {

    private final AdminOrderRepository adminOrderRepository;

    public AdminOrderStats getStatistics() {
        LocalDateTime from = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = LocalDateTime.now();
        List<AdminOrder> orders = adminOrderRepository.findAllByPlaceDateIsBetweenAndOrderStatus(
                from, to, OrderStatus.COMPLETED);

        TreeMap<Integer, AdminOrderStatsValue> result = IntStream.rangeClosed(from.getDayOfMonth(), to.getDayOfMonth())
                                                                 .boxed()
                                                                 .map(i -> aggregateValues(i, orders))
                                                                 .collect(toMap(
                                                                         value -> value.day(),
                                                                         value -> value,
                                                                         (t, t2) -> {
                                                                             throw new IllegalArgumentException();
                                                                         },
                                                                         TreeMap::new));

        List<Long> orderList = result.values().stream()
                .map(v -> v.orders()).toList();
        List<BigDecimal> salesList = result.values().stream()
                .map(v -> v.sales()).toList();

        return AdminOrderStats.builder()
                              .label(result.keySet()
                                           .stream()
                                           .toList())
                              .sale(result.values()
                                          .stream()
                                          .map(o -> o.sales)
                                          .toList())
                              .order(result.values()
                                           .stream()
                                           .map(o -> o.orders)
                                           .toList())
                              .ordersCount(orderList.stream()
                                                    .reduce(Long::sum)
                                                    .orElse(0L))
                              .salesSum(salesList.stream()
                                                 .reduce(BigDecimal::add)
                                                 .orElse(BigDecimal.ZERO))
                              .build();
    }

    private AdminOrderStatsValue aggregateValues(int i, List<AdminOrder> orders) {

        return orders.stream()
                     .filter(adminOrder -> adminOrder.getPlaceDate()
                                                     .getDayOfMonth() == i)
                     .map(AdminOrder::getGrossValue)
                     .reduce(
                             new AdminOrderStatsValue(i, BigDecimal.ZERO, 0L),
                             (AdminOrderStatsValue o, BigDecimal v) -> new AdminOrderStatsValue(i, o.sales.add(v), o.orders + 1),
                             (o, o2) -> null
                     );
    }

    private record AdminOrderStatsValue(Integer day, BigDecimal sales, Long orders) {
    }
}