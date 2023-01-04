package com.ex.shop.cart.service;

import com.ex.shop.cart.model.Cart;
import com.ex.shop.cart.model.dto.CartProductDto;
import com.ex.shop.cart.repository.CartRepository;
import com.ex.shop.common.model.Product;
import com.ex.shop.common.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// 3.0 test
// 5.0 mockito:
@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    // 5.2 mockuję te repozytoria:
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;


    // 5.1 klasą, do której będą wstrzykiwane zmockowane repozytoria będzie CartService, więc dodaję adnotację:
    @InjectMocks
    private CartService cartService;

    @Test
    void shouldAddProductToCartWhenCartIdNotExists() {
        // given
        Long cartId = 0L;
        CartProductDto cartProductDto = new CartProductDto(1L, 1);
        // 6.0 dodaję when:
        when(productRepository.findById(1L)).thenReturn(Optional.of(Product.builder()
                .id(1L)
                .build())); // mam już zmockowaną metodę findById w ProductRepository

        // mockito uruchomi tę metodę dla dowolnego argumentu
        when(cartRepository.save(any())).thenReturn((Cart.builder()
                .id(1L)
                .build())); // mam już zmockowaną metodę findById w ProductRepository
        // when
        Cart result = cartService.addProductToCart(cartId, cartProductDto);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void shouldAddProductToCartWhenCartIdExists() { // 6.1 test robi prwie to samo, tylko id jest ustawione od 1:
        // given
        Long cartId = 1L; // skoro istnieje, to już od jedynki
        CartProductDto cartProductDto = new CartProductDto(1L, 1);
        when(productRepository.findById(1L)).thenReturn(Optional.of(Product.builder()
                .id(1L)
                .build()));

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(Cart.builder()
                .id(1L)
                .build()));
        // when
        Cart result = cartService.addProductToCart(cartId, cartProductDto);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }
}