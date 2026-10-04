package com.example.najvecaLolcina.Controller;

import com.example.najvecaLolcina.Entity.CreateReviewRequest;
import com.example.najvecaLolcina.Service.ReviewService;
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

    @PostMapping("/review")
    public void addReview(@RequestBody @Valid CreateReviewRequest createReviewRequest){
        reviewService.addReview(createReviewRequest);
    }


}
