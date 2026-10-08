package com.example.najvecaLolcina.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","product_id"}))
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rate")
    private int rate;

    @Column(name = "description")
    private String description;

    @Column(name = "time")
    private LocalDateTime localDateTime;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private MyyyUser myyyUser;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Review() {
    }

    public Review(int rate, String description, LocalDateTime localDateTime) {

        this.rate = rate;
        this.description = description;
        this.localDateTime = localDateTime;
    }


    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public MyyyUser getMyyyUser() {
        return myyyUser;
    }

    public void setMyyyUser(MyyyUser myyyUser) {
        this.myyyUser = myyyUser;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}
