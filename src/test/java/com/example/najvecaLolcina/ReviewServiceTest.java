package com.example.najvecaLolcina;

import com.example.najvecaLolcina.entity.*;
import com.example.najvecaLolcina.repository.OrderItemRepository;
import com.example.najvecaLolcina.repository.ProductRepository;
import com.example.najvecaLolcina.repository.ReviewRepository;
import com.example.najvecaLolcina.security.MyyyUserRepo;
import com.example.najvecaLolcina.service.ProductService;
import com.example.najvecaLolcina.service.ReviewService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private MyyyUserRepo myyyUserRepo;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;


    private MyyyUser user;

    @BeforeEach
    void setUp() {
        user = new MyyyUser();
        user.setId(1L);
        user.setName("testUser");

        var authentication = new UsernamePasswordAuthenticationToken(
                "testUser", null, List.of()
        );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        when(myyyUserRepo.findMyyyUserByName("testUser"))
                .thenReturn(user);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void addReviewTestException() {

        CreateReviewRequest createReviewRequest = new CreateReviewRequest(1L, 5, "Good product");

        when(productRepository.findByIdUpdate(createReviewRequest.getProductId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, ()-> {reviewService.addReview(createReviewRequest);});

        verify(productRepository).findByIdUpdate(createReviewRequest.getProductId());
    }

    @Test
    void addReviewTestExceptionTwo(){

        var product = new Product("lol", 10, BigDecimal.valueOf(10), "lol");
        product.setId(1L);

        CreateReviewRequest createReviewRequest = new CreateReviewRequest(1L, 5, "Good product");

        when(productRepository.findByIdUpdate(createReviewRequest.getProductId())).thenReturn(Optional.of(product));
        when(orderItemRepository.findOrderItemMethod(product, user, OrderStatus.DELIVERED)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {reviewService.addReview(createReviewRequest);});

        verify(productRepository).findByIdUpdate(createReviewRequest.getProductId());
        verify(orderItemRepository).findOrderItemMethod(product, user, OrderStatus.DELIVERED);
        verify(reviewRepository, never()).save(any(Review.class));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void addReviewTestExceptionThree(){

        var product = new Product("lol", 10, BigDecimal.valueOf(10), "lol");
        product.setId(1L);

        CreateReviewRequest createReviewRequest = new CreateReviewRequest(1L, 5, "Good product");

        OrderItem orderItem = new OrderItem(BigDecimal.valueOf(10), 5);

        when(productRepository.findByIdUpdate(createReviewRequest.getProductId())).thenReturn(Optional.of(product));
        when(orderItemRepository.findOrderItemMethod(product, user, OrderStatus.DELIVERED)).thenReturn(Optional.of(orderItem));
        when(reviewRepository.existsByMyyyUserAndProduct(user, product)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, ()-> {reviewService.addReview(createReviewRequest);});

        verify(productRepository).findByIdUpdate(createReviewRequest.getProductId());
        verify(orderItemRepository).findOrderItemMethod(product, user, OrderStatus.DELIVERED);
        verify(reviewRepository).existsByMyyyUserAndProduct(user, product);
        verify(reviewRepository, never()).save(any(Review.class));
        verify(productRepository, never()).save(any(Product.class));

    }

    @Test
    void addReviewExceptionFull(){

        var product = new Product("lol", 10, BigDecimal.valueOf(10), "lol");
        product.setId(1L);

        CreateReviewRequest createReviewRequest = new CreateReviewRequest(1L, 5, "Good product");

        OrderItem orderItem = new OrderItem(BigDecimal.valueOf(10), 5);

        when(productRepository.findByIdUpdate(createReviewRequest.getProductId())).thenReturn(Optional.of(product));
        when(orderItemRepository.findOrderItemMethod(product, user, OrderStatus.DELIVERED)).thenReturn(Optional.of(orderItem));
        when(reviewRepository.existsByMyyyUserAndProduct(user, product)).thenReturn(false);

        reviewService.addReview(createReviewRequest);

        ArgumentCaptor<Review> reviewCaptor =
                ArgumentCaptor.forClass(Review.class);

        verify(reviewRepository).save(reviewCaptor.capture());

        Review savedReview = reviewCaptor.getValue();

        assertEquals(5, savedReview.getRate());
        assertEquals("Good product", savedReview.getDescription());
        assertEquals(user, savedReview.getMyyyUser());
        assertEquals(product, savedReview.getProduct());

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(captor.capture());

        Product savedProduct = captor.getValue();

        assertEquals(savedProduct.getRatingCount(), 1);
        assertEquals(savedProduct.getTotalScore(), createReviewRequest.getRate());
        assertEquals(savedProduct.getAverageRating(), createReviewRequest.getRate());
    }












}