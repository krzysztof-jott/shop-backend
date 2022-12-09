package com.ex.shop.product.controller;

import com.ex.shop.product.model.Product;
import com.ex.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

@RestController // jak dodamy endpointy, to stworzy dla nich mapowanie
@RequiredArgsConstructor // dodaję adnotację i mogę usunąć konstruktor poniżej
// 25.4UP dodaję adnotację:
@Validated
public class ProductController {

    private final ProductService productService; // dodanie adnotacji jest równoznaczne z = new ProductService. Finale, bo nigdy nie będzie nadpisywane

    @GetMapping("/products")
    public Page<Product> getProducts(@PageableDefault(size = 15) Pageable pageable) { // 5.0 adnotacja zmienia domyślną wartość Pageable (20)
        return productService.getProducts(pageable); // 3.1 zamieniam Listę na Page
    }

    // 25.0UP tworzę usługę, która pobierze jeden produkt po jego slugu:
    @GetMapping("/products/{slug}")
    public Product getProduct(    // 25.3UP robię walidację tego sluga, który przychodzi z zewnątrz:
                                  @PathVariable
                                  @Pattern(regexp = "[a-z0-9\\-]+") // wzorzec, który będzie pasował do sluga. Litery od a do z, cyfry
                                  // od 0 do 9 i myślnik, + czyli może być taki znak 1 lub wiele
                                  @Length(max = 255)
                                  String slug) {

        return productService.getProduct(slug); // 25.1UP tworzę metodę w serwisie
    }
}