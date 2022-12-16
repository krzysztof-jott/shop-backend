package com.ex.shop.category.repository;

import com.ex.shop.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> { // wstrzykuję do serwisu
}