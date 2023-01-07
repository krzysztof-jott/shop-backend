package com.ex.shop.cart.service;

import com.ex.shop.cart.model.Cart;
import com.ex.shop.cart.repository.CartItemRepository;
import com.ex.shop.cart.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
// 24.0 dodaję adnotacje do loga:
@Slf4j
@RequiredArgsConstructor
public class CartCleanupService {

    private final CartRepository cartRepository;
    // 25.0 wstrzykuję repozytorium:
    private final CartItemRepository cartItemRepository;

    // 23.0 tworzę metodę, dodaję adnotację: */10 - sekundy (co dziesiąta sekunda), potem gwiazdka - minuty, godziny,
    // dni miesiąca, miesiące, dzień tygodnia
    // dodaję specjalne wyrażenie przed tym:
    // 25.4 dodaję adnotację Transactional
    @Transactional
    @Scheduled(cron = "${app.cart.cleanup.expression}") // i w propertisach dodaję te gwiazdki

    //    @Scheduled(cron = "*/10 * * * * *")
    /*@Scheduled(cron = "0 0 3 * * *") // odpali się codziennie o 3 w nocy*/
    public void cleanupOldCarts() { // pobieram wszystkie koszyki starsze niż 3 dni:
        List<Cart> carts = cartRepository.findByCreatedLessThan(LocalDateTime.now().minusDays(3));
        // 26.0 pobieram id streamem:
        List<Long> ids = carts.stream() // 26.1 przypisuję to zmiennej ids
                .map(Cart::getId)
                .toList(); // i są już id pobrane
        log.info("stare koszyki " + carts.size()); // co 10 sekund wyświetla się log ile jest starych koszyków
        carts.forEach(cart -> {
            // 26.2 zakomentowuję, robię nowe zapytania
            /*cartItemRepository.deleteByCartId(cart.getId()); // tworzę tę metodę w repozytorium
            // 25.2 dodaję kolejne zapytanie do usunięcia samego koszyka:
            cartRepository.deleteCartById(cart.getId()); // tworzę w repozytorium
        });*/

            // 26.3 nowe:
            if (!ids.isEmpty()) { // jeśli ta tablica nie jest pyta, to wykonuje zapytanie:
                cartItemRepository.deleteAllByCartIdIn(ids); // tworzę metodę
                // 26.5 druga metoda:
                cartRepository.deleteAllByIdIn(ids);
            }
        });
    }
}