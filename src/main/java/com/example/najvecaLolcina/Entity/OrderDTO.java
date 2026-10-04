package com.example.najvecaLolcina.Entity;

import com.example.najvecaLolcina.OrderStatus;

import java.time.LocalDateTime;

public class OrderDTO {

    private Long id;
    private double totalPrice;
    private LocalDateTime dateTime;
    private OrderStatus status;

    public OrderDTO(Long id, double totalPrice, LocalDateTime dateTime, OrderStatus status) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.dateTime = dateTime;
        this.status = status;
    }

    public OrderDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
