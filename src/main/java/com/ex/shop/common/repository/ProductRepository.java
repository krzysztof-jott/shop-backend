package com.ex.shop.common.repository;

import com.ex.shop.common.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository // można dodać adnotację, ale nie trzeba, bo Spring rozpozna po dziedziczeniu, że to repo
public interface ProductRepository extends JpaRepository<Product, Long> { // będzie pobierać produkty z bazy danych dla tej encji
    Optional<Product> findBySlug(String slug);

    Page<Product> findByCategoryId(Long id, Pageable pageable); // Spring Data zwróci już postronicowane produkty

    List<Product> findTop10BySalePriceIsNotNull();
}