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

// 26.1
@Service
@AllArgsConstructor
public class AdminOrderStatsService {// wstrzykuję w kontrolerze

    private final AdminOrderRepository adminOrderRepository;

    public AdminOrderStats getStatistics() {
        LocalDateTime from = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime to = LocalDateTime.now();
        List<AdminOrder> orders = adminOrderRepository.findAllByPlaceDateIsBetweenAndOrderStatus( // przerabiam na zmienne from, to, żeby
                // wyznaczyć zakres dat
                from, to, OrderStatus.COMPLETED); // zapisuję do DTO:

        // rangeClosed - ostatni dzień zakresu też ma należeć:
        TreeMap<Integer, AdminOrderStatsValue> result = IntStream.rangeClosed(from.getDayOfMonth(), to.getDayOfMonth())
                // strumień generuje int i nie mogę go użyć jako klucza mapy, muszę go zamienić na Integer:
                .boxed()
                // pobieram zagregowane wartości i mapuje na obiekt:
                .map(i -> aggregateValues(i, orders))
                .collect(toMap(
                        value -> value.day(),
                        value -> value,
                        (t, t2) -> {
                            throw new IllegalArgumentException();
                        },
                        TreeMap::new));

//        TreeMap<Integer, AdminOrderStatsValue> result = new TreeMap<>();
        // pętla po dniach:
        /*for (int i = from.getDayOfMonth(); i <= to.getDayOfMonth(); i++) {
            // tworzę metodę prywatną:
            result.put(i, aggregateValues(i, orders));
        }*/
        List<Long> orderList = result.values().stream()
                .map(v -> v.orders()).toList();
        List<BigDecimal> salesList = result.values().stream()
                .map(v -> v.sales()).toList();

        // muszę podzielić mapę i zwrócić w DTO:
        return AdminOrderStats.builder()
                .label(result.keySet().stream().toList())   // wyciągam klucze, które będą dniami, wyrzuci posortowany set z kluczami map
                .sale(result.values().stream().map(o -> o.sales).toList())
                .order(result.values().stream().map(o -> o.orders).toList())
                .ordersCount(orderList.stream().reduce(Long::sum).orElse(0L))
                .salesSum(salesList.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO))
                .build();
    }

    private AdminOrderStatsValue aggregateValues(int i, List<AdminOrder> orders) {

        return orders.stream() // strumień dla zamówień
                .filter(adminOrder -> adminOrder.getPlaceDate().getDayOfMonth() == i) // filtruję po dacie
                .map(AdminOrder::getGrossValue)
                .reduce(
                        // podaję obiekt, do któego będę redukować wynik:
                        new AdminOrderStatsValue(i, BigDecimal.ZERO, 0L),
                        // akumulator BiFunction, agregujętu wartości:
                        (AdminOrderStatsValue o, BigDecimal v) -> new AdminOrderStatsValue(i, o.sales.add(v), o.orders + 1),
                        //  combiner, wykorzystywany w strumieniach równoległych, nie używam ich więc null
                        (o, o2) -> null
                );

       /* BigDecimal totalValue = BigDecimal.ZERO;
        Long orderCount = 0L;
        // iteracja po orders:
        for (AdminOrder order : orders) {
            // jeśli dzień się zgadza, będę agregować dane z takiego dnia:
            if (i == order.getPlaceDate().getDayOfMonth()) { // lista jest nieposortowana
                totalValue.add(order.getGrossValue());
                orderCount++;
            }
        }*/
//        return new AdminOrderStatsValue(totalValue, orderCount); // wypełniła się teraz mapa TreeMap
    }

    private record AdminOrderStatsValue(Integer day, BigDecimal sales, Long orders) {
    }
}