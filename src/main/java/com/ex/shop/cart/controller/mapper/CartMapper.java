package com.ex.shop.cart.controller.mapper;

import com.ex.shop.cart.controller.dto.CartSummaryDto;
import com.ex.shop.cart.controller.dto.CartSummaryItemDto;
import com.ex.shop.cart.controller.dto.ProductDto;
import com.ex.shop.cart.controller.dto.SummaryDto;
import com.ex.shop.common.model.Cart;
import com.ex.shop.common.model.CartItem;
import com.ex.shop.common.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class CartMapper { // 8.0
    public static CartSummaryDto mapToCartSummary(Cart cart) {
        // 8.2 mapuję na CartSummaryDto:
        return CartSummaryDto.builder()
                .id(cart.getId())
                // 8.3 robię nową metodę:
                .items(mapCartItems(cart.getItems()))
                // 8.4 kolejna metoda:
                .summary(mapToSummary(cart.getItems()))
                .build();
    }

    public static List<CartSummaryItemDto> mapCartItems(List<CartItem> items) {
        // 8.5 mapuję items:
        return items.stream()
                // 8.6 kolejna metoda:
                .map(CartMapper::mapToCartItem)
                .toList();
    }

    private static CartSummaryItemDto mapToCartItem(CartItem cartItem) {
        return CartSummaryItemDto.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                // 8.7 kolejna metoda:
                .product(mapToProductDto(cartItem.getProduct()))
                // 8.8 kolejna metoda:
                .lineValue(calculateLineValue(cartItem))
                .build();
    }

    private static ProductDto mapToProductDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .currency(product.getCurrency())
                .image(product.getImage())
                .slug(product.getSlug())
                .build();
    }

    private static BigDecimal calculateLineValue(CartItem cartItem) {
        //8.9 quantity to int, więc muszę zamienić na BigDecimal:
        return cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())); // wyliczona wartość pozycji
    }

    private static SummaryDto mapToSummary(List<CartItem> items) {
        return SummaryDto.builder()
                // 8.10 kolejna metoda:
                .grossValue(sumValues(items))
                .build();
    }

    private static BigDecimal sumValues(List<CartItem> items) {
        return items.stream()
                .map(CartMapper::calculateLineValue)
                .reduce(BigDecimal::add)
                // 17.1 usuwam Throw i dodaję BigDecimal, bo wyskakuje błąd wyjątku przy pustym koszyku
                .orElse(BigDecimal.ZERO);
    }
}