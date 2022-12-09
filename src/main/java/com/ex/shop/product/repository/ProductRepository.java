package com.ex.shop.product.repository;

import com.ex.shop.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // można dodać adnotację, ale nie trzeba, bo Spring rozpozna po dziedziczeniu, że to repo
public interface ProductRepository extends JpaRepository<Product, Long> { // będzie pobierać produkty z bazy danych dla tej encji
    // 25.5UP dodaję metodę. Korzystam z już zaimplementowanej metody:
    /*Optional<Product> findById(Long id);*/ // Optional dlatego, że mogę wyszukać coś po slugu, a tego produktu nie będzie. USUWAM

    // i zostawiam jednak tą metodę i wracam do serwisu:
    Optional<Product> findBySlug(String slug);
}