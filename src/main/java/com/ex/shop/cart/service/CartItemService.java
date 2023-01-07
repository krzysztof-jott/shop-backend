package com.ex.shop.cart.service;

import com.ex.shop.cart.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }

    // 19.3 dodaję metodę:
    public Long countItemInCart(Long cartId) {
        return cartItemRepository.countByCartId(cartId); // tworzę w repozytorium tę metodę
    }
}