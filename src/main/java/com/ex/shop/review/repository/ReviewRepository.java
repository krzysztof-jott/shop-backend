package com.ex.shop.review.repository;

import com.ex.shop.common.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}