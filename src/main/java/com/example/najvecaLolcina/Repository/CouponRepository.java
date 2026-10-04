package com.example.najvecaLolcina.Repository;

import com.example.najvecaLolcina.Entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findCouponByCoupon(String coupon);

}
