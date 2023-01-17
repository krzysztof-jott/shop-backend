package com.ex.shop.common.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    // 2.1 dodaję relację (1 element w koszyku odpowiada 1 produktowi)
    @OneToOne
    private Product product;
    private Long cartId;
}