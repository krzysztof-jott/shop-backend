package com.ex.shop.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // baza danych generuje wartości tej kolumny
    private Long id;
    private String name;
    private Long categoryId; // 38.3 dodaję pole
    private String description;
    private String fullDescription;
    private BigDecimal price;
    private String currency;
    private String image;
    private String slug;
    // 44.1 dodaję pole i oznaczam relację i dodaję joina:
    @OneToMany
    @JoinColumn(name = "productId") // productId, bo w Review jest takie pole
    private List<Review> reviews;
}