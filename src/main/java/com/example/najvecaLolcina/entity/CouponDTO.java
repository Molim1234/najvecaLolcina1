package com.example.najvecaLolcina.entity;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class CouponDTO {
    @NotBlank
    private String coupon;
    @Min(0)
    @Max(100)
    private int percentOfDscount;
    @Min(0)
    private int uses;
   @DateTimeFormat
   @Future
    private LocalDateTime expiringDate;


    public CouponDTO(String coupon, int percentOfDscount, int uses, LocalDateTime expiringDate) {
        this.coupon = coupon;
        this.percentOfDscount = percentOfDscount;
        this.uses = uses;
        this.expiringDate = expiringDate;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public int getPercentOfDscount() {
        return percentOfDscount;
    }

    public void setPercentOfDscount(int percentOfDscount) {
        this.percentOfDscount = percentOfDscount;
    }

    public int getUses() {
        return uses;
    }

    public void setUses(int uses) {
        this.uses = uses;
    }

    public LocalDateTime getExpiringDate() {
        return expiringDate;
    }

    public void setExpiringDate(LocalDateTime expiringDate) {
        this.expiringDate = expiringDate;
    }
}
