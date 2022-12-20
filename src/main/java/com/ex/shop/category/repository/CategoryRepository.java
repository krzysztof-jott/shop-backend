package com.ex.shop.category.repository;

import com.ex.shop.common.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // 37.0 dodaję adnotację. Wpisuję zapytanie z joinem, żeby pobrać wszystko jednym zapytaniem:
    // 38.0 teraz nie chcę używać zapytania, żeby pobierać wszystko razem, tylko chcę pobrać samą kategorię, usuwam @Query, idę do encji
    /*@Query("select c from Category c " +
            "left join fetch c.product " + // trzeba użyć fetch, żeby było tylko jedno zapytanie
            "where c.slug=:slug") // zwróci kategorię razem z produktami JEDNYM zapytaniem. Nie da się go stronicować też (tzn da,
    // ale za pomocą czystego SQL i zagnieżdzonych zapytań, niewygodne.*/
    Category findBySlug(String slug);
}