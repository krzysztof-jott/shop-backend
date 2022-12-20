package com.ex.shop.review.controller;

import com.ex.shop.common.model.Review;
import com.ex.shop.review.controller.dto.ReviewDto;
import com.ex.shop.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    // 42.3 wstrzykuję serwis:
    private final ReviewService reviewService;

    @PostMapping("/reviews") // mapowanie postem, bo zapisuję nową opinię za każdym razem
    public Review addReview(@RequestBody @Valid ReviewDto reviewDto) { // 44.1 dodaję @Valid
        // muszę przemapować DTO na encję Review. Do encji dodaję Buildera
        return reviewService.addReview(Review.builder()
                .authorName(cleanContent(reviewDto.authorName()))
                .content(cleanContent(reviewDto.content()))
                .productId(reviewDto.productId())
                .build()); // i już jest zmapowana encja Review
    }

    private String cleanContent(String text) {
        return Jsoup.clean(text, Safelist.none()); // 43.0 wklejam z dokumentacji JSOUP, unsafe zamieniam na text i basic na none,
        // chcę mieć tylko czysty tekst
    }
}