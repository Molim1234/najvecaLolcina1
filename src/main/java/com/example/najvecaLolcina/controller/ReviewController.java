package com.example.najvecaLolcina.controller;

import com.example.najvecaLolcina.entity.CreateReviewRequest;
import com.example.najvecaLolcina.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/reviews")
    public void addReview(@RequestBody @Valid CreateReviewRequest createReviewRequest){
        reviewService.addReview(createReviewRequest);
    }


}
