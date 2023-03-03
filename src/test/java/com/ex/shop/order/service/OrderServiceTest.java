package com.ex.shop.order.service;

import com.ex.shop.common.mail.EmailClientService;
import com.ex.shop.common.mail.FakeEmailService;
import com.ex.shop.common.model.Cart;
import com.ex.shop.common.model.CartItem;
import com.ex.shop.common.model.OrderStatus;
import com.ex.shop.common.model.Product;
import com.ex.shop.common.repository.CartItemRepository;
import com.ex.shop.common.repository.CartRepository;
import com.ex.shop.order.model.Payment;
import com.ex.shop.order.model.PaymentType;
import com.ex.shop.order.model.Shipment;
import com.ex.shop.order.model.dto.OrderDto;
import com.ex.shop.order.model.dto.OrderSummary;
import com.ex.shop.order.repository.OrderRepository;
import com.ex.shop.order.repository.OrderRowRepository;
import com.ex.shop.order.repository.PaymentRepository;
import com.ex.shop.order.repository.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

//    private static final LocalDateTime PLACE_DATE = LocalDateTime.now(); nie testuję daty, jest mało istotna
    @Mock
    private CartRepository cartRepository; // 46.0
    @Mock
    private ShipmentRepository shipmentRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderRowRepository orderRowRepository; // tego już
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private EmailClientService emailSender;
    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldPlaceOrder() {
        //given
        OrderDto orderDto = createOrderDto();
        // 46.1 dodaję when i metodę prywatną createCart():
        when(cartRepository.findById(any())).thenReturn(createCart());
        when(shipmentRepository.findById(any())).thenReturn(createShipment());
        when(paymentRepository.findById(any())).thenReturn(createPayment());
        // jeśli to save() będzie przekazany jakikolwiek parametr, to mam do niego dostęp przez invocation i w [] odpowiedni
        // indeks tablicy:
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(emailSender.getInstance()).thenReturn(new FakeEmailService());

        Long userId = 1L;
        //when
        OrderSummary orderSummary = orderService.placeOrder(orderDto, userId);
        //then
        assertThat(orderSummary).isNotNull();
        assertThat(orderSummary.getStatus()).isEqualTo(OrderStatus.NEW);
//        assertThat(orderSummary.getPlaceDate()).isEqualTo(PLACE_DATE); nie testuję daty, jest mało istotna
        assertThat(orderSummary.getGrossValue()).isEqualTo(new BigDecimal("36.22"));
        assertThat(orderSummary.getPayment().getType()).isEqualTo(PaymentType.BANK_TRANSFER);
        assertThat(orderSummary.getPayment().getName()).isEqualTo("test payment");
        assertThat(orderSummary.getPayment().getId()).isEqualTo(1L);
    }

    private Optional<Payment> createPayment() {
        return Optional.of(Payment.builder()
                .id(1L)
                .name("test payment")
                .type(PaymentType.BANK_TRANSFER)
                .defaultPayment(true)
                .build());
    }

    private Optional<Shipment> createShipment() {
        return Optional.of(Shipment.builder()
                .id(1L)
                .price(new BigDecimal("14.00"))
                .build());
    }

    private Optional<Cart> createCart() {
        return Optional.of(Cart.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .items(createItems())
                .build());
    }

    // 46.2:
    private List<CartItem> createItems() {
        return List.of(
                CartItem.builder()
                        .id(1L)
                        .cartId(1L)
                        .quantity(1)
                        .product(Product.builder()
                                .id(1L)
                                .price(new BigDecimal("11.11"))
                                .build())
                        .build(),
                CartItem.builder()
                        .id(2L)
                        .cartId(1L)
                        .quantity(1)
                        .product(Product.builder()
                                .id(2L)
                                .price(new BigDecimal("11.11"))
                                .build())
                        .build());
    }

    private OrderDto createOrderDto() {
        return OrderDto.builder()
                .firstname("firstname")
                .lastname("lastname")
                .street("street")
                .zipcode("zipcode")
                .city("city")
                .email("email")
                .phone("phone")
                .cartId(1L)
                .shipmentId(2L)
                .paymentId(3L)
                .build();
    }
}