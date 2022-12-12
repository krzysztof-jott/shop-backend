package com.ex.shop.admin.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "product") // podaję nazwę, jakiej tabeli tyczy się ta klasa
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor // trzeba dodać, bo z @Builder pojawia się błąd
public class AdminProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
// 16.0 zmieniam pole na Long i dodaję Id
    private Long categoryId;
    private String description;
    private String fullDescription;
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    private AdminProductCurrency currency;
    private String image;
    private String slug;
}