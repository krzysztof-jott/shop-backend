package com.ex.shop.common.repository;

import com.ex.shop.common.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByCreatedLessThan(LocalDateTime minusDays);

    // 25.3 tworzę metodę i drugie query:
    @Query("delete from Cart c where c.id=:id")
    @Modifying
    void deleteCartById(Long id);

    // 26.6 tworzę metodę i drugie query:
    @Query("delete from Cart c where c.id in (:ids)")
    @Modifying
    void deleteAllByIdIn(List<Long> ids);
}