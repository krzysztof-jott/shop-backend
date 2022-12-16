package com.ex.shop.category.controlller;

import com.ex.shop.category.model.Category;
import com.ex.shop.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 29.0 tworzę standardowo wszystkie metody kontrolera:
    @GetMapping
    public List<Category> getCategories() { // tworzę encję Category w modelu
        return categoryService.getCategories();
    }
}