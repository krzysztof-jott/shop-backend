package com.ex.shop.cart.controller;

import com.ex.shop.cart.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cartItems")
@RequiredArgsConstructor
public class CartItemController { // 16.0

    private final CartItemService cartItemService;

    @DeleteMapping("/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartItemService.delete(id);
    }
}