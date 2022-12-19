package com.ex.shop.category.model;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String slug;
    // 34.0 dodaję:
    // 38.1 usuwam pole z produktami i idę do serwisu:
//    @OneToMany
//    @JoinColumn(name = "categoryId")
//    private List<Product> product;
}