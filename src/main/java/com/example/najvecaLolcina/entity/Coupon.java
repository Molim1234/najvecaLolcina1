package com.example.najvecaLolcina.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "coupon", unique = true)
    private String coupon;
    @Column(name = "percent_of_discount")
    private int percentOfDscount;
    @Column(name = "uses")
    private int uses;
    @Column(name = "expiringDate")
    private LocalDateTime expiringDate;

    public Coupon(String coupon, int percentOfDscount, int uses, LocalDateTime expiringDate) {
        this.coupon = coupon;
        this.percentOfDscount = percentOfDscount;
        this.uses = uses;
        this.expiringDate = expiringDate;
    }

    public Coupon() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
