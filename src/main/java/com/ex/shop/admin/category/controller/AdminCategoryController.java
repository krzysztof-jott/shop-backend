package com.ex.shop.admin.category.controller;

import com.ex.shop.admin.category.controller.dto.AdminCategoryDto;
import com.ex.shop.admin.category.model.AdminCategory;
import com.ex.shop.admin.category.service.AdminCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ex.shop.admin.common.utils.SlugifyUtils.slugifySlug;

@RestController
// 6.1 żeby nie wpisywać ciągle tego samego w mappingach, tutaj dodaję część wspólną dla wszystkich metod:
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private static final Long EMPTY_ID = null;
    // 9.0 wstrzykuję serwis:
    private final AdminCategoryService adminCategoryService;

    // 6.0 potrzebuję metody do odczytu listy, odczytu pojedynczej kategorii, zapisu, edycji i usuwania kategorii:
    @GetMapping
    public List<AdminCategory> getCategories() {
        return adminCategoryService.getCategories(); // 9.1 w prostym CRUDzie nazwy metod mogą być identyczne
    }

    @GetMapping("/{id}")
    public AdminCategory getCategory(@PathVariable Long id) {
        return adminCategoryService.getCategory(id);
    }

    @PostMapping // 7.1 dodaję adnotację dla DTO:
    public AdminCategory createCategory(@RequestBody AdminCategoryDto adminCategoryDto) { // 10.0 tworzę prywatną metodę mapującą:
        return adminCategoryService.createCategory(mapToAdminCategory(EMPTY_ID, adminCategoryDto));
    }

    // 10.1 metodą mapująca. Zamieniam Object na AdminCategory:
    private AdminCategory mapToAdminCategory(Long id, AdminCategoryDto adminCategoryDto) {
        return AdminCategory.builder() // 10.2 mapuję wszystkie pola:
                .id(id)
                .name(adminCategoryDto.getName())
                .description(adminCategoryDto.getDescription())
                // zmieniam na slugifySlug:
                .slug(slugifySlug(adminCategoryDto.getSlug())) // 10.3 trzeba przerobić sluga, żeby faktycznie był slugiem,
                // a nie tym co wpisze użytkownik. Tworzę prywatną metodę slugifyCategoryName
                .build();
    }

/*    // 10.4 prywatna metoda:
    private String slugifyCategoryName(String slug) {
        Slugify slugify = new Slugify();
        return slugify.withCustomReplacement("_", "-").slugify(slug);
    }*/

    @PutMapping("/{id}") // 7.2 dodaję adnotację dla DTO:
    public AdminCategory updateCategory(@PathVariable Long id, @RequestBody AdminCategoryDto adminCategoryDto) {
        return adminCategoryService.updateCategory(mapToAdminCategory(id, adminCategoryDto));
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        adminCategoryService.deleteCategory(id);
    }
}