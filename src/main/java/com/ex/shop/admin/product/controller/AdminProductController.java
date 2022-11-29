package com.ex.shop.admin.product.controller;

import com.ex.shop.admin.product.controller.dto.AdminProductDto;
import com.ex.shop.admin.product.model.AdminProduct;
import com.ex.shop.admin.product.service.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

// 12.0 tworzę nową klasę:
@RestController
@RequiredArgsConstructor
public class AdminProductController {

    private static final Long EMPTY_ID = null;
    // 13.0 wstrzykuję serwis:
    private final AdminProductService productService;

    @GetMapping("/admin/products")
    public Page<AdminProduct> getProducts(Pageable pageable) { // 13.1 też dodaję Pageable
        return productService.getProducts(pageable);
    }

    // 16.0 dodaję metodę:
    @GetMapping("/admin/products/{id}")
    public AdminProduct getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    // 16.3 dodawanie nowych produktów. Tworzenie nowych zasobów:
    @PostMapping("/admin/products")
    public AdminProduct createProduct(@RequestBody @Valid AdminProductDto adminProductDto) { // 16.5 muszę tu stworzyć jakiś obiekt. Tworzę DTO
        return productService.createProduct(mapAdminProduct(adminProductDto, EMPTY_ID) // 16.4 w serwisie będzie nowa metoda createProduct. Będę potrzebował
                // 17.2 tworzenie stałej EMPTY_ID nie jest konieczne, jest dla lepszej czytelości, żeby było widać, że to jest puste id, a nie null
                // @RequestBody wyżej i obiektu, który przyjmie to co będzie wysyłać usługa.
                // 16.9 w ten sposób możemy dodawać poszczególne pola i przemapuję to co jest w DTO na encję:
                // 17.1 zwijam to wszystko do metody mapAdminProduct:
/*                .name(adminProductDto.getName())
                .description(adminProductDto.getDescription())
                .category(adminProductDto.getCategory())
                .price(adminProductDto.getPrice())
                .currency(adminProductDto.getCurrency())
                .build() // w ten sposób przemapowałem DTO na AdminProduct. Tworzę teraz metodę serwisową*/
        );
    }
    // 17.0 usługa do zapisywania edytowanych produktów. Edycja zasobów:
    @PutMapping("/admin/products/{id}")
    public AdminProduct updateProduct(@RequestBody @Valid AdminProductDto adminProductDto, @PathVariable Long id) { // 17.1 będzie potrzebne jeszcze id
        return productService.updateProduct(mapAdminProduct(adminProductDto, id) // 31.0 bez @Valid Hibernate Validator nie sprawdzi
                // klasy. Po to żeby wyzwolić uruchomienia Hibernate Validator
        );
    }

    private static AdminProduct mapAdminProduct(AdminProductDto adminProductDto, Long id) {
        return AdminProduct.builder() // to akurat w takiej formie (bo w DTO nie ma id?)
                .id(id)
                .name(adminProductDto.getName())
                .description(adminProductDto.getDescription())
                .category(adminProductDto.getCategory())
                .price(adminProductDto.getPrice())
                .currency(adminProductDto.getCurrency()/*.toUpperCase(Locale.ROOT)  32. usuwam po walidacji*/)
                .build(); // wyekstrachowana metoda, bo powtarza sie to samo w reszcie metod, więc zeby ciągle nie pisać tego samego
    }
}