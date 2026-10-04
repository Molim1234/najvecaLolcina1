package com.example.najvecaLolcina.Entity;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
