package com.ex.shop.order.service;

import com.ex.shop.common.mail.EmailClientService;
import com.ex.shop.common.model.Cart;
import com.ex.shop.common.repository.CartItemRepository;
import com.ex.shop.common.repository.CartRepository;
import com.ex.shop.order.model.Order;
import com.ex.shop.order.model.Payment;
import com.ex.shop.order.model.Shipment;
import com.ex.shop.order.model.dto.OrderDto;
import com.ex.shop.order.model.dto.OrderListDto;
import com.ex.shop.order.model.dto.OrderSummary;
import com.ex.shop.order.repository.OrderRepository;
import com.ex.shop.order.repository.OrderRowRepository;
import com.ex.shop.order.repository.PaymentRepository;
import com.ex.shop.order.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static com.ex.shop.order.service.mapper.OrderDtoMapper.mapToOrderListDto;
import static com.ex.shop.order.service.mapper.OrderEmailMessageMapper.createEmailMessage;
import static com.ex.shop.order.service.mapper.OrderMapper.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderRowRepository orderRowRepository;
    private final CartItemRepository cartItemRepository;
    private final ShipmentRepository shipmentRepository;
    private final PaymentRepository paymentRepository;
    private final EmailClientService emailClientService;

    @Transactional
    public OrderSummary placeOrder(OrderDto orderDto, Long userId) {

        Cart cart = cartRepository.findById(orderDto.getCartId()).orElseThrow();
        Shipment shipment = shipmentRepository.findById(orderDto.getShipmentId()).orElseThrow();
        Payment payment = paymentRepository.findById(orderDto.getPaymentId()).orElseThrow();
        Order newOrder = orderRepository.save(createNewOrder(orderDto, cart, shipment, payment, userId));
        saveOrderRows(cart, newOrder.getId(), shipment);
        clearOrderCart(orderDto);
        sendConfirmEmail(newOrder);
        log.info("Zamówienie złożone");
        return createOrderSummary(payment, newOrder);
    }

    private void sendConfirmEmail(Order newOrder) {
        emailClientService.getInstance()
                          .send(newOrder.getEmail(),
                                  "Twoje zamówienie zostało przyjęte",
                                  createEmailMessage(newOrder));
    }

    private void clearOrderCart(OrderDto orderDto) {
        cartItemRepository.deleteByCartId(orderDto.getCartId());
        cartRepository.deleteCartById(orderDto.getCartId());
    }

    private void saveOrderRows(Cart cart, Long orderId, Shipment shipment) {
        saveProductRows(cart, orderId);
        saveShipmentRow(orderId, shipment);
    }

    private void saveShipmentRow(Long orderId, Shipment shipment) {
        orderRowRepository.save(mapToOrderRow(orderId, shipment));
    }

    private void saveProductRows(Cart cart, Long orderId) {
        cart.getItems()
            .stream()
            .map(cartItem -> mapToOrderRowWithQuantity(orderId, cartItem)
            )
            .peek(orderRowRepository::save)
            .toList();
    }

    public List<OrderListDto> getOrdersForCustomer(Long userId) {
        return mapToOrderListDto(orderRepository.findByUserId(userId));
    }
}