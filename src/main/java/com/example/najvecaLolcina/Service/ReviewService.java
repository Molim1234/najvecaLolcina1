package com.example.najvecaLolcina.Service;

import com.example.najvecaLolcina.Entity.CreateReviewRequest;
import com.example.najvecaLolcina.Entity.MyyyUser;
import com.example.najvecaLolcina.Entity.Review;
import com.example.najvecaLolcina.OrderStatus;
import com.example.najvecaLolcina.Repository.OrderItemRepository;
import com.example.najvecaLolcina.Repository.ProductRepository;
import com.example.najvecaLolcina.Repository.ReviewRepository;
import com.example.najvecaLolcina.Security.MyyyUserRepo;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class ReviewService {

    private MyyyUserRepo myyyUserRepo;
    private ProductRepository productRepository;
    private OrderItemRepository orderItemRepository;
    private ReviewRepository reviewRepository;

    public ReviewService(MyyyUserRepo myyyUserRepo, ProductRepository productRepository, OrderItemRepository orderItemRepository, ReviewRepository reviewRepository) {
        this.myyyUserRepo = myyyUserRepo;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public void addReview(CreateReviewRequest createReviewRequest) {
        var user = returnAuthenticatedUser();

        var product = productRepository.findById(createReviewRequest.getProductId()).orElseThrow(()->new NoSuchElementException("There is no rpoduct with this id"+createReviewRequest.getProductId()));

        var orderItem = orderItemRepository.findOrderItemMethod(product, user, OrderStatus.DELIVERED).orElseThrow(()->new NoSuchElementException("This user didnt get this product"));

        var review = new Review(createReviewRequest.getRate(),
                createReviewRequest.getDescription(), LocalDateTime.now()
                );
        review.setMyyyUser(user);
        review.setProduct(product);
        reviewRepository.save(review);

        product.setRatingCount(product.getRatingCount()+1);
        product.setTotalScore(product.getTotalScore()+ createReviewRequest.getRate());
        product.setAverageRating((double) product.getTotalScore()/ product.getRatingCount());
        productRepository.save(product);

    }





    public MyyyUser returnAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userr = authentication.getName();
        return myyyUserRepo.findMyyyUserByName(userr);
    }





}
