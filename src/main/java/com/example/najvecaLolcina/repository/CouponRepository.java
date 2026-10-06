package com.example.najvecaLolcina.repository;

import com.example.najvecaLolcina.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findCouponByCoupon(String coupon);

}
