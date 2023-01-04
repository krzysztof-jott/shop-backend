package com.ex.shop.cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;
    // 2.0 dodaję odpowiednie powiązanie, czyli zmapować powiązanie do CartItem:
    @OneToMany(cascade = CascadeType.PERSIST) // 4.6 mówię Hibernetowi, żeby wraz z zapisaniem koszyka, zapisywał też listę elementów
    @JoinColumn(name = "cartId")
    private List<CartItem> items;

    // 4.0 dodaję metodę pomocniczą:
    public void addProduct(CartItem cartItem) {
        // 4.1 muszę sprawdzić, czy ta tablica jest zainicjalizowana, robię null checka:
        if (items == null) {
            items = new ArrayList<>();
        }
        // 12.4 filtruję listę w koszyku, czy są już te same produkty dodane, jeśli tak to ma się zwiększać quantity
        items.stream()
                .filter(item -> Objects.equals(cartItem.getProduct().getId(), item.getProduct().getId()))
                // 12.5 szukam pierwszego elementu, zwraca Optionala:
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + 1),
                        // 12.6 jeśli element nie istnieje to:
                        () -> items.add(cartItem)
                );
    }
}