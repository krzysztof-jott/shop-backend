package com.ex.shop.product.controller;

import com.ex.shop.product.model.Product;
import com.ex.shop.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController // jak dodamy endpointy, to stworzy dla nich mapowanie
@RequiredArgsConstructor // dodaję adnotację i mogę usunąć konstruktor poniżej
public class ProductController {

    /*@Autowired // wstrzykiwanie przez pole. Usuwam i robię przez konstruktor niżej*/
    private final ProductService productService; // dodanie adnotacji jest równoznaczne z = new ProductService. Finale, bo nigdy nie będzie nadpisywane

    // usuwam po dodaniu adnotacji RequiredArgsCunstruktor:
    /*public ProductController(ProductService productService) {
        this.productService = productService;
    }*/

    // 32.0 tworzę pierwszą metodę, która wystawi listę produktów:
    // 32.1 dodaję mapowanie dla usługi:
    @GetMapping("/products")
    public List<Product> getProducts() {
        return productService.getProducts(); // 38.5 korzystam z metody serwisowej i przechodzę do serwisu

//        38.4 usuwam to i używam wyzej medoty getProducts z serwisu:
        /*return List.of( // to co zwracam z metody jest od razu konwertowane na jsona:
                new Product("Nowy produkt1", "Nowa kategoria1", "Nowy opis1", new BigDecimal(3), "PLN"),
                new Product("Nowy produkt2", "Nowa kategoria2", "Nowy opis2", new BigDecimal(4), "PLN"),
                new Product("Nowy produkt3", "Nowa kategoria3", "Nowy opis3", new BigDecimal(5), "PLN"),
                new Product("Nowy produkt4", "Nowa kategoria4", "Nowy opis4", new BigDecimal(6), "PLN")
        ); // tworzę tymczasową listę produktów*/
    }
}
