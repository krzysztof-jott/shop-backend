package com.ex.shop.cart.service;

import com.ex.shop.cart.model.Cart;
import com.ex.shop.cart.model.CartItem;
import com.ex.shop.cart.model.dto.CartProductDto;
import com.ex.shop.cart.repository.CartRepository;
import com.ex.shop.common.model.Product;
import com.ex.shop.common.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public Cart getCart(Long id) {
        return cartRepository.findById(id).orElseThrow();
    }

    // 4.7 dodaję adnotację:
    @Transactional
    public Cart addProductToCart(Long id, CartProductDto cartProductDto) {
        Cart cart = getInitializedCart(id);// 3.3 wydzielona metoda
        // 4.3
        cart.addProduct(CartItem.builder()
                        .quantity(cartProductDto.quantity())
                // 4.4 tworzę metodę pomocniczą getProduct
                        .product(getProduct(cartProductDto.productId()))
                // 4.5 ustawiam cartId:
                        .cartId(cart.getId())
                .build());
        return cart;
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow();
    }

    private Cart getInitializedCart(Long id) {
        if (id == null || id <= 0) { // 3.1 jeśli null to tworzę koszyk, dla zerowego id też się ma utworzyć
            return cartRepository.save(Cart.builder()
                    .created(now())
                    .build()); // 3.2 wydzielam kod do prywatnej metody
        }
        // jeśli koszyk istnieje:
        return cartRepository.findById(id).orElseThrow();
    }
}