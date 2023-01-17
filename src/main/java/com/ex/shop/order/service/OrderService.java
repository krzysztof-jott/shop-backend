package com.ex.shop.order.service;

import com.ex.shop.common.model.Cart;
import com.ex.shop.common.model.CartItem;
import com.ex.shop.common.repository.CartItemRepository;
import com.ex.shop.common.repository.CartRepository;
import com.ex.shop.order.model.Order;
import com.ex.shop.order.model.OrderRow;
import com.ex.shop.order.model.OrderStatus;
import com.ex.shop.order.model.dto.OrderDto;
import com.ex.shop.order.model.dto.OrderSummary;
import com.ex.shop.order.repository.OrderRepository;
import com.ex.shop.order.repository.OrderRowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    // 12.0 wstrzykuję repo:
    private final OrderRowRepository orderRowRepository;
    // 13.1 wstrzykuję repo:
    private final CartItemRepository cartItemRepository;

    // 10.0 implementuję metodę. Muszę stworzyć zamówienie z wierszami, pobrać koszyk i na jego podstawie zrobić wiersze,
    // zapisać zamówienie, usunąć koszyk i zwrócić podsumowanie:
    @Transactional
    public OrderSummary placeOrder(OrderDto orderDto) {

        // 12.1 przeniesione Cart
        Cart cart = cartRepository.findById(orderDto.getCartId()).orElseThrow();
        // 10.1 tworzę zamówienie:
        Order order = Order.builder()
                .firstname(orderDto.getFirstname())
                .lastname(orderDto.getLastname())
                .street(orderDto.getStreet())
                .zipcode(orderDto.getZipcode())
                .city(orderDto.getCity())
                .email(orderDto.getEmail())
                .phone(orderDto.getPhone())
                .placeDate(LocalDateTime.now())
                .orderStatus(OrderStatus.NEW)
                // 12.3 robię metodę pomocniczą:
                .grossValue(calculateGrossValue(cart.getItems()))
                .build();
        // zapisywanie zamówienia:
        Order newOrder = orderRepository.save(order);
        // 10.2 do stworzenia wierszy zamówień będę potrzebował pobrać zawartość koszyka, czyli to co jest tutaj i
        // będę potrzebować cen produktów i ich ilości w koszyku.Żeby pobrać koszyk, mogę skorzystać z repozytorium koszyka,
        // i najlepiej jak je przeniosę do common, bo będę je współdzielić.
        // 11.0 pobieram koszyk:
        // 12.0 przenoszę Cart do góry:
//        Cart cart = cartRepository.findById(orderDto.getCartId()).orElseThrow();
        // 11.1 żeby stworzyć wiersze zamówienia z pozycji koszyka tworzę osobna metodę gdzie te kolejne wiersze zapisze:
        saveOrderRows(cart, newOrder.getId()); // przekazuję koszyk i id nowego zamówienia

        // 13.0 usuwanie koszyka. Najpierw muszę usunąć elementy koszyka:
        cartItemRepository.deleteByCartId(orderDto.getCartId());
        cartRepository.deleteCartById(orderDto.getCartId());
        // 13.2 zwracam summary:
        return OrderSummary.builder()
                .id(newOrder.getId())
                .placeDate(newOrder.getPlaceDate())
                .status(newOrder.getOrderStatus())
                .grossValue(newOrder.getGrossValue())
                .build();
    }

    private BigDecimal calculateGrossValue(List<CartItem> items) {
        return items.stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                // quantity jest int więc trzeba go zamienić na BigDecimal
                // muszę użyć reduce, żeby zredukować wszystkie BigDecimale do jednego BigDecimala:
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    // 11.2 metoda:
    private void saveOrderRows(Cart cart, Long orderId) {
        cart.getItems().stream()
                .map(cartItem -> OrderRow.builder() // dodaję wszystkie pola, które są potrzebne:
                        .quantity(cartItem.getQuantity())
                        .productId(cartItem.getProduct().getId())
                        .price(cartItem.getProduct().getPrice())
                        .orderId(orderId)
                        .build()
                )
                .peek(orderRowRepository::save) // metoda pozwala dodać konsumera
                .toList();
    }
}