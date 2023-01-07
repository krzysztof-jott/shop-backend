package com.ex.shop.cart.controller;

import com.ex.shop.cart.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cartItems")
@RequiredArgsConstructor
public class CartItemController { // 16.0

    private final CartItemService cartItemService;

    @DeleteMapping("/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartItemService.delete(id);
    }

    //    19.2 dodaję usługę do zliczania liczby rzeczy w koszyku:
    @GetMapping("/count/{cartId}")
    public Long countItemInCart(@PathVariable Long cartId) {
        return cartItemService.countItemInCart(cartId); // dodaję metodę w serwisie
    }
}