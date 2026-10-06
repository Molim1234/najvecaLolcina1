package com.example.najvecaLolcina.entity;


import java.math.BigDecimal;

public class OrderItemDTO {

    private Long id;
    private String productType;
    private String productName;
    private BigDecimal price;
    private int qunatity;
    private BigDecimal totalPrice;

    public OrderItemDTO(Long id, String productType, String productName, BigDecimal price, int qunatity, BigDecimal totalPrice) {
        this.id = id;
        this.productType = productType;
        this.productName = productName;
        this.price = price;
        this.qunatity = qunatity;
        this.totalPrice = totalPrice;
    }

    public OrderItemDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQunatity() {
        return qunatity;
    }

    public void setQunatity(int qunatity) {
        this.qunatity = qunatity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
