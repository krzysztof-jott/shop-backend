package com.ex.shop.product.repository;

import com.ex.shop.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // można dodać adnotację, ale nie trzeba, bo Spring rozpozna po dziedziczeniu, że to repo
public interface ProductRepository extends JpaRepository<Product, Long> { // będzie pobierać produkty z bazy danych dla tej encji

    Optional<Product> findBySlug(String slug);
}