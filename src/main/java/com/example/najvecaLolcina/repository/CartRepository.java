package com.example.najvecaLolcina.repository;

import com.example.najvecaLolcina.entity.Cart;
import com.example.najvecaLolcina.entity.MyyyUser;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Cart c where c.myyyUser = :user")
    Optional<Cart> findByUserForUpdate(@Param("user") MyyyUser user);

}
