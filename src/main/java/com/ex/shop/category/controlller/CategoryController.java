package com.ex.shop.category.controlller;

import com.ex.shop.category.dto.CategoryProductsDto;
import com.ex.shop.category.service.CategoryService;
import com.ex.shop.common.model.Category;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    // 29.0 tworzę standardowo wszystkie metody kontrolera:
    @GetMapping
    public List<Category> getCategories() { // tworzę encję Category w modelu
        return categoryService.getCategories();
    }

    @GetMapping("/{slug}/products")
    public CategoryProductsDto getCategoriesWithProducts(@PathVariable // dodaję walidację
                                              @Pattern(regexp = "[a-z0-9\\-]+")
                                              @Length(max = 255) String slug, Pageable pageable) { // 38.0 dodaję Pageable
        // tu też muszę dostosować to co zwracam, zamienia się u góry na Dto:
        return categoryService.getCategoriesWithProducts(slug, pageable); // tu przekazuję pageable i idę do serwisu
    }
}