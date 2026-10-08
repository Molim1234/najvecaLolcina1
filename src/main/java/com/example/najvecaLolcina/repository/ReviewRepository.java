package com.example.najvecaLolcina.repository;

import com.example.najvecaLolcina.entity.MyyyUser;
import com.example.najvecaLolcina.entity.Product;
import com.example.najvecaLolcina.entity.Review;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByMyyyUserAndProduct(MyyyUser myyyUser, Product product);
}
