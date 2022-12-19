package com.ex.shop.category.service;

import com.ex.shop.category.model.Category;
import com.ex.shop.category.model.CategoryProductsDto;
import com.ex.shop.category.repository.CategoryRepository;
import com.ex.shop.product.model.Product;
import com.ex.shop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true) // 39 readOnly stosujemy, gdy w metodzie nie zapisujemy encji, żadnych zapisów, aktualizacji encji. Hibernate
    // nie śledzi wtedy zmian w encjach jak jest true. Dzięki temu transakcja działa trochę szybciej. Mechanizm Dirty Check w Hibernate jest wyłączony
    public CategoryProductsDto getCategoriesWithProducts(String slug, Pageable pageable) { // 38.1 tu też dodaję Pagealbe
        // zamiast return, przypisuję całość do zmiennej category;
        Category category = categoryRepository.findBySlug(slug);
        // teraz pobieram produkty z danej kategorii i postronicuję je. Dodaję Page<> i wtedy metoda w serwisie już zwróci określony typ:
        Page<Product> page = productRepository.findByCategoryId(category.getId(), pageable); // dodaję pageable, żeby SpringData od razu postronicowało wyniki
        // 38.4 te zapytania 2 powyżej powinny już pobrać i kategorie i produkty dla tej kategorii już postronicowane, muszę jeszcze
        // połączyć te wyniki. Robię DTO, które zbierze mi te dane.
        // 39.0 zwracam z DTO:
        return new CategoryProductsDto(category, page); // dostosowuję to co zwracam i Category z metody zmienia się na CategoryProductsDto
    }
}