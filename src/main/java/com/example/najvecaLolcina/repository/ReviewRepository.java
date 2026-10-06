package com.example.najvecaLolcina.repository;

import com.example.najvecaLolcina.entity.MyyyUser;
import com.example.najvecaLolcina.entity.Product;
import com.example.najvecaLolcina.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByMyyyUserAndProduct(MyyyUser myyyUser, Product product);
}
