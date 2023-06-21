package com.ex.shop.cart.service;

import com.ex.shop.common.model.Cart;
import com.ex.shop.common.repository.CartItemRepository;
import com.ex.shop.common.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartCleanupService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    @Scheduled(cron = "${app.cart.cleanup.expression}")
    /*@Scheduled(cron = "0 0 3 * * *") // odpali się codziennie o 3 w nocy*/
    public void cleanupOldCarts() { // pobieram wszystkie koszyki starsze niż 3 dni:
        List<Cart> carts = cartRepository.findByCreatedLessThan(LocalDateTime.now().minusDays(3));
        List<Long> ids = carts.stream()
                              .map(Cart::getId)
                              .toList();
        log.info("stare koszyki " + carts.size());

            if (!ids.isEmpty()) {
                cartItemRepository.deleteAllByCartIdIn(ids);
                cartRepository.deleteAllByIdIn(ids);
            }
    }
}