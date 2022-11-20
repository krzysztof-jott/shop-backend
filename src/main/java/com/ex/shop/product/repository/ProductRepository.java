package com.ex.shop.product.repository;

import com.ex.shop.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // można dodać adnotację, ale nie trzeba, bo Spring rozpozna po dzieczeniu, że to repo
public interface ProductRepository extends JpaRepository<Product, Long> { // będzie pobierać produkty z bazy danych dla tej encji

}
