package com.ex.shop.cart.controller;

import com.ex.shop.cart.controller.dto.CartSummaryDto;
import com.ex.shop.cart.controller.mapper.CartMapper;
import com.ex.shop.cart.model.dto.CartProductDto;
import com.ex.shop.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 2.2 usługa, która będzie pobierała koszyk po jego id
    @GetMapping("/{id}")
    public CartSummaryDto getCart(@PathVariable Long id) { // potem będę musiał przemapować na DTO, bo potrzebuję tylko części informacji
        // 8.1 zamieniam cartService na CartMapper i dodaję metodę mapToCartSummary
        return CartMapper.mapToCartSummary(cartService.getCart(id));
    }

    // 2.3 dodaję kolejną usługę:
    @PutMapping("/{id}") // id koszyka
    public CartSummaryDto addProductToCart(@PathVariable Long id, @RequestBody CartProductDto cartProductDto) {
        // 8.11 zmieniam:
        return CartMapper.mapToCartSummary(cartService.addProductToCart(id, cartProductDto));
    }

    // 13.0 tworzę metodę aktualizacji koszyka:
    @PutMapping("/{id}/update")
    public CartSummaryDto updateCart(@PathVariable Long id, @RequestBody List<CartProductDto> cartProductDtos) {
        // 13.1 listę Dtosów przekazuję do metody serwisowej, tworzę metodę updateCart(), bedzię przyjmowałą id i listę
        // Dtosów. Przekazuję liste Dtos, bo będę przemapowywać to dto na listę pobranych encji.
        return CartMapper.mapToCartSummary(cartService.updateCart(id, cartProductDtos));
    }
}