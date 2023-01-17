package com.ex.shop.common.repository;

import com.ex.shop.common.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 19.4 tworzę metodę i Spring Data zwróci wartość zliczonych wierszy:
    Long countByCartId(Long cartId);

    // 25.1 tworzę metodę i query z zapytaniem delete:
    @Query("delete from CartItem ci where ci.cartId=:cartId")
    @Modifying
    void deleteByCartId(Long cartId);

    // 26.4 dodaję metodę i adnotację z query:
    @Query("delete from CartItem ci where ci.cartId in (:ids)")
    @Modifying
    void deleteAllByCartIdIn(List<Long> ids);
}