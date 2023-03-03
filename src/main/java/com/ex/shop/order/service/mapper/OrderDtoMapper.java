package com.ex.shop.order.service.mapper;

import com.ex.shop.order.model.Order;
import com.ex.shop.order.model.dto.OrderListDto;

import java.util.List;

public class OrderDtoMapper {

    // 52.1 przeniosłem tu metodę i zmieniam private na pulic static:
    public static List<OrderListDto> mapToOrderListDto(List<Order> orders) {
        // 51.3 przemapowuję za pomocą strumienia:
        return orders.stream()
                .map(order -> new OrderListDto(
                        order.getId(),
                        order.getPlaceDate(),
                        // 56.0 robię przemapowanie, dodaję getValue()
                        order.getOrderStatus().getValue(),
                        order.getGrossValue()))
                .toList();
    }
}