package com.ex.shop.admin.product.repository;

import com.ex.shop.admin.product.model.AdminProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProductRepository extends JpaRepository<AdminProduct, Long> {
}