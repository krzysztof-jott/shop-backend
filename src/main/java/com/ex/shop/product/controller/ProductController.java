package com.ex.shop.product.controller;

import com.ex.shop.product.model.Product;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController // jak dodamy endpointy, to stworzy dla nich mapowanie
public class ProductController {

    // 32.0 tworzę pierwszą metodę, która wystawi listę produktów:
    // 32.1 dodaję mapowanie dla usługi:
    @GetMapping("/products")
    public List<Product> getProducts() {
        return List.of( // to co zwracam z metody jest od razu konwertowane na jsona:
                new Product("Nowy produkt1", "Nowa kategoria1", "Nowy opis1", new BigDecimal(3), "PLN"),
                new Product("Nowy produkt2", "Nowa kategoria2", "Nowy opis2", new BigDecimal(4), "PLN"),
                new Product("Nowy produkt3", "Nowa kategoria3", "Nowy opis3", new BigDecimal(5), "PLN"),
                new Product("Nowy produkt4", "Nowa kategoria4", "Nowy opis4", new BigDecimal(6), "PLN")
        ); // tworzę tymczasową listę produktów
    }
}
