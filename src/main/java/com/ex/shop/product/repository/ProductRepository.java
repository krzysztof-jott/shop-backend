package com.ex.shop.product.repository;

import com.ex.shop.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // można dodać adnotację, ale nie trzeba, bo Spring rozpozna po dziedziczeniu, że to repo
public interface ProductRepository extends JpaRepository<Product, Long> { // będzie pobierać produkty z bazy danych dla tej encji

    Optional<Product> findBySlug(String slug);

    // 38.1 żeby ta metoda zadziałała, muszę dodać odpowiednie pole w encji
    Page<Product> findByCategoryId(Long id, Pageable pageable); // Spring Data zwróci już postronicowane produkty
}