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

public class CartMapper {
    public static CartSummaryDto mapToCartSummary(Cart cart) {
        return CartSummaryDto.builder()
                             .id(cart.getId())
                             .items(mapCartItems(cart.getItems()))
                             .summary(mapToSummary(cart.getItems()))
                             .build();
    }

    public static List<CartSummaryItemDto> mapCartItems(List<CartItem> items) {
        return items.stream()
                    .map(CartMapper::mapToCartItem)
                    .toList();
    }

    private static CartSummaryItemDto mapToCartItem(CartItem cartItem) {
        return CartSummaryItemDto.builder()
                                 .id(cartItem.getId())
                                 .quantity(cartItem.getQuantity())
                                 .product(mapToProductDto(cartItem.getProduct()))
                                 .lineValue(calculateLineValue(cartItem))
                                 .build();
    }

    private static ProductDto mapToProductDto(Product product) {
        return ProductDto.builder()
                         .id(product.getId())
                         .name(product.getName())
                         .price(product.getEndPrice())
                         .currency(product.getCurrency())
                         .image(product.getImage())
                         .slug(product.getSlug())
                         .build();
    }

    private static BigDecimal calculateLineValue(CartItem cartItem) {
        return cartItem.getProduct().getEndPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())); // wyliczona wartość pozycji
    }

    private static SummaryDto mapToSummary(List<CartItem> items) {
        return SummaryDto.builder()
                         .grossValue(sumValues(items))
                         .build();
    }

    private static BigDecimal sumValues(List<CartItem> items) {
        return items.stream()
                    .map(CartMapper::calculateLineValue)
                    .reduce(BigDecimal::add)
                    .orElse(BigDecimal.ZERO);
    }
}