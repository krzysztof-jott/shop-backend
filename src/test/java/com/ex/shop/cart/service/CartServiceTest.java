package com.ex.shop.cart.service;

import com.ex.shop.cart.model.dto.CartProductDto;
import com.ex.shop.common.model.Cart;
import com.ex.shop.common.model.Product;
import com.ex.shop.common.repository.CartRepository;
import com.ex.shop.common.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private CartService cartService;

    @Test
    void shouldAddProductToCartWhenCartIdNotExists() {
        // given
        Long cartId = 0L;
        CartProductDto cartProductDto = new CartProductDto(1L, 1);
        // when
        when(productRepository.findById(1L)).thenReturn(Optional.of(Product.builder()
                                                                           .id(1L)
                                                                           .build()));
        when(cartRepository.save(any())).thenReturn((Cart.builder()
                                                         .id(1L)
                                                         .build()));
        Cart result = cartService.addProductToCart(cartId, cartProductDto);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void shouldAddProductToCartWhenCartIdExists() {
        // given
        Long cartId = 1L;
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