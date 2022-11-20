package com.ex.shop.product.service;

import com.ex.shop.product.model.Product;
import com.ex.shop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    // 38.5 żeby ta metoda zadziałała, trzeba wstrzyknąć repozytorium
    private final ProductRepository productRepository;

    public List<Product> getProducts() {
        // 38.6 mogę teraz użyc repozytorium:
        return productRepository.findAll(); // zwracam wszystkie produkty. Dzięki tej metodzie Hibernate wykona zapytanie, które pobierze listę wszystkich produktów
    }
}