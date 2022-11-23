package com.ex.shop.product.service;

import com.ex.shop.product.model.Product;
import com.ex.shop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<Product> getProducts(Pageable pageable) { // 5.4 zamieniam na Pageable tak jak w kontrolerze
        return productRepository.findAll(pageable); // 4.0 dodaję PageRequest z metodą of, strona nr 1 będzie mieć 25 rekordów.
        // metoda zwraca Page, a nie listę, dlatego jestt bład.
    }
}