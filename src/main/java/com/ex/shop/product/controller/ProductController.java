package com.ex.shop.product.controller;

import com.ex.shop.product.model.Product;
import com.ex.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // jak dodamy endpointy, to stworzy dla nich mapowanie
@RequiredArgsConstructor // dodaję adnotację i mogę usunąć konstruktor poniżej
public class ProductController {

    private final ProductService productService; // dodanie adnotacji jest równoznaczne z = new ProductService. Finale, bo nigdy nie będzie nadpisywane

    @GetMapping("/products")
    public Page<Product> getProducts(@PageableDefault(size = 15) Pageable pageable) { // 5.0 adnotacja zmienia domyślną wartość Pageable (20)
        return productService.getProducts(pageable); // 3.1 zamieniam Listę na Page
    }
}
