package com.ex.shop.review.service;

import com.ex.shop.common.model.Review;
import com.ex.shop.common.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    // 42.0 wstrzykuję repozytorium:
    private final ReviewRepository reviewRepository;

    public Review addReview(Review review) {
        return reviewRepository.save(review); // 42.1 przez save zapisuję opinię, idę do kontrolera
    }
}