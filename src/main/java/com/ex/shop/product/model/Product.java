package com.ex.shop.product.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor // 38.1 konstruktor bezargumetowy
public class Product { // 38.0 encja musi mieć domyślny bezargumentowy konstruktor
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 38.2 baza danych generuje wartości tej kolumny
    private Long id; // 38.3 w kontrolerze pojawi się błąd, do dałem pole id, a tam go nie ma
    private String name;
    private String category;
    private String description;
    private BigDecimal price;
    private String currency;
    //    20.0UP dodaję pola. W części administracyjnej też muszę je dodać:
    private String image;
    private String slug;
}