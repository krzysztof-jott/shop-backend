package com.ex.shop.review.controller;

import com.ex.shop.common.model.Review;
import com.ex.shop.review.controller.dto.ReviewDto;
import com.ex.shop.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews") // mapowanie postem, bo zapisuję nową opinię za każdym razem
    public Review addReview(@RequestBody @Valid ReviewDto reviewDto) {// muszę przemapować DTO na encję Review
        return reviewService.addReview(Review.builder()
                .authorName(cleanContent(reviewDto.authorName()))
                .content(cleanContent(reviewDto.content()))
                .productId(reviewDto.productId())
                .build()); // i już jest zmapowana encja Review
    }

    private String cleanContent(String text) {
        return Jsoup.clean(text, Safelist.none()); // wklejam z dokumentacji JSOUP, unsafe zamieniam na text i basic na none,
    }
}