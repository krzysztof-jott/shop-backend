package com.ex.shop.admin.product.service;

import com.ex.shop.admin.product.model.AdminProduct;
import com.ex.shop.admin.product.repository.AdminProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final AdminProductRepository productRepository;

    public Page<AdminProduct> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    // 16.1 teraz metoda w serwisie. Pobieranie pojedynczego produktu:
    public AdminProduct getProduct(Long id) {
        return productRepository.findById(id).orElseThrow(); // 16.2 jest błąd, bo metoda findById zwraca Optionala, więc dodaję wyjątek po
        // kropce. Jeśli w Optionalu istnieje AdminProduct to metoda go pobierze, jeśli nie wyrzuci wyjątek.
    }

    public AdminProduct createProduct(AdminProduct product) {
        return productRepository.save(product);
    }

    public AdminProduct updateProduct(AdminProduct product) {
        return productRepository.save(product); // ta sama metoda co w createProduct
     }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}