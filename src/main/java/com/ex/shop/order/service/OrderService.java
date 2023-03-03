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
    // 12.0 wstrzykuję repo:
    private final OrderRowRepository orderRowRepository;
    // 13.1 wstrzykuję repo:
    private final CartItemRepository cartItemRepository;
    // 22.0
    private final ShipmentRepository shipmentRepository;
    private final PaymentRepository paymentRepository;
    // 35.1 zmieniam EmailSimpleSender na EmailSender;
    // 40.0 zmieniam EmailSender na EmailClientService:
    private final EmailClientService emailClientService;


    // 10.0 implementuję metodę. Muszę stworzyć zamówienie z wierszami, pobrać koszyk i na jego podstawie zrobić wiersze,
    // zapisać zamówienie, usunąć koszyk i zwrócić podsumowanie:
    @Transactional
    public OrderSummary placeOrder(OrderDto orderDto, Long userId) {

        // 12.1 przeniesione Cart
        Cart cart = cartRepository.findById(orderDto.getCartId()).orElseThrow();
        // 23.1 przeniesiony shipment:
        Shipment shipment = shipmentRepository.findById(orderDto.getShipmentId()).orElseThrow();
        // pobieram sposób płatności:
        Payment payment = paymentRepository.findById(orderDto.getPaymentId()).orElseThrow();
        // 10.1 tworzę zamówienie:
        // 44.0 wydzielam order do metody prywatnej i inlajnuje ten order i ten poniżej:
        // zapisywanie zamówienia:
        Order newOrder = orderRepository.save(createNewOrder(orderDto, cart, shipment, payment, userId));
        // 10.2 do stworzenia wierszy zamówień będę potrzebował pobrać zawartość koszyka, czyli to co jest tutaj i
        // będę potrzebować cen produktów i ich ilości w koszyku.Żeby pobrać koszyk, mogę skorzystać z repozytorium koszyka,
        // i najlepiej jak je przeniosę do common, bo będę je współdzielić.
        // 11.0 pobieram koszyk:
        // 12.0 przenoszę Cart do góry:
//        Cart cart = cartRepository.findById(orderDto.getCartId()).orElseThrow();

        // 22.1 pobieram sposób dostawy:
        // 23.0 przenoszę shipment wyżej:
//        Shipment shipment = shipmentRepository.findById(orderDto.getShipmentId()).orElseThrow();
        // 11.1 żeby stworzyć wiersze zamówienia z pozycji koszyka tworzę osobna metodę gdzie te kolejne wiersze zapisze:
        // 22.2 przekazuję shipment:
        saveOrderRows(cart, newOrder.getId(), shipment); // przekazuję koszyk i id nowego zamówienia

        // 13.0 usuwanie koszyka. Najpierw muszę usunąć elementy koszyka:
        // 44.2 wydzielam metodę do czyszczenia koszyka:
        clearOrderCart(orderDto);
        // 34.0 chcę wysłać wiadomość, że zamówienie zostało złożone:
        //34.1 tworzę metodę prywatną:
        // 40.1 poprawiam z emailSender na emailClientService i dodaję getInstance. Może to być którakolwiek instacja z tych serwisów:
        // 44.3 wydzielam do metody prywatnej:
        sendConfirmEmail(newOrder);
        log.info("Zamówienie złożone");
        // 13.2 zwracam summary:
        // 44.1 wydzielam do metody prywatnej:
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

/*    private static OrderSummary createOrderSummary(Payment payment, Order newOrder) {
        return OrderSummary.builder()
                .id(newOrder.getId())
                .placeDate(newOrder.getPlaceDate())
                .status(newOrder.getOrderStatus())
                .grossValue(newOrder.getGrossValue())
                // zmieniam podsumowanie, najpierw w OrderSummary muszę dodać paymanet:
                .payment(payment)
                .build();
    }*/

/*    private Order createNewOrder(OrderDto orderDto, Cart cart, Shipment shipment, Payment payment) {
        return Order.builder()
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
                // 23.2 dodaję parametr shipment:
                .grossValue(calculateGrossValue(cart.getItems(), shipment))
                // 28.0 dodaję:
                .payment(payment)
                .build();
    }*/

 /*   private String createEmailMessage(Order order) {
        // 34.2 wklejam treść:
        return "Twoje zamówienie o id: " + order.getId() +
                "\nData złożenia: " + order.getPlaceDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                "\nWartość: " + order.getGrossValue() + " PLN " +
                "\n\n" +
                "\nPłatność: " + order.getPayment().getName() +
                (order.getPayment().getNote() != null ? "\n" + order.getPayment().getNote() : "") +
                "\n\nDziękujemy za zakupy.";
    }*/

/*    private BigDecimal calculateGrossValue(List<CartItem> items, Shipment shipment) {
        return items.stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                // quantity jest int więc trzeba go zamienić na BigDecimal
                // muszę użyć reduce, żeby zredukować wszystkie BigDecimale do jednego BigDecimala:
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO)
                // 23.3 dodaję cenę dostawy:
                .add(shipment.getPrice());
    }*/

    // 11.2 metoda:
    private void saveOrderRows(Cart cart, Long orderId, Shipment shipment) {
        saveProductRows(cart, orderId);

        // 22.3 dodaję do metody (teraz jest wydzialona do prywatnej):
        saveShipmentRow(orderId, shipment);
    }

    // 22.5 wydzielam do prywatnej metody:
    private void saveShipmentRow(Long orderId, Shipment shipment) {
        orderRowRepository.save(mapToOrderRow(orderId, shipment));
    }

/*    private static OrderRow mapToOrderRow(Long orderId, Shipment shipment) {
        return OrderRow.builder()   // buduję sposób dostawy:
                .quantity(1)
                .price(shipment.getPrice())
                .shipmentId(shipment.getId())
                .orderId(orderId)
                .build();
    }*/

    // 22.4 wydzieliłem do prywatnej metody:
    private void saveProductRows(Cart cart, Long orderId) {
        cart.getItems().stream()
                .map(cartItem -> mapToOrderRowWithQuantity(orderId, cartItem)
                )
                .peek(orderRowRepository::save) // metoda pozwala dodać konsumera
                .toList();
    }


    /*private static OrderRow mapToOrderRowWithQuantity(Long orderId, CartItem cartItem) {
        return OrderRow.builder() // dodaję wszystkie pola, które są potrzebne:
                .quantity(cartItem.getQuantity())
                .productId(cartItem.getProduct().getId())
                .price(cartItem.getProduct().getPrice())
                .orderId(orderId)
                .build();
    }*/

    public List<OrderListDto> getOrdersForCustomer(Long userId) {
        // 50.1 dorabiam metodę serwisową, która te zamówienia będzie zwracała:
        // 51.1 dodaję metodę prywatną mapTo...:
        return mapToOrderListDto(orderRepository.findByUserId(userId)); // i metoda w repozytorium
    }

    // 51.2 tutaj już zwracam OrderListDto, a nie Order, ogólnie wszędzie tu zmieniam teraz Order na OrderListDto:
    // 52.0 przenoszę metodę do mappera OrderDtoMapper, żeby tu nie zaśmiecać:
 /*   private List<OrderListDto> mapToOrderListDto(List<Order> orders) {
        // 51.3 przemapowuję za pomocą strumienia:
        return orders.stream()
                .map(order -> new OrderListDto(
                        order.getId(),
                        order.getPlaceDate(),
                        order.getOrderStatus(),
                        order.getGrossValue()))
                .toList();
    }*/
}