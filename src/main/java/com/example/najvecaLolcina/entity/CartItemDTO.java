package com.example.najvecaLolcina.entity;

import java.math.BigDecimal;

public class CartItemDTO {

    private Long id;
    private String productType;
    private String productName;
    private int quantityInCart;
    private BigDecimal totalPrice;

    public CartItemDTO(Long id, String productType, String productName, int quantityInCart, BigDecimal totalPrice) {
        this.id = id;
        this.productType = productType;
        this.productName = productName;
        this.quantityInCart = quantityInCart;
        this.totalPrice = totalPrice;
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

    public int getQuantityInCart() {
        return quantityInCart;
    }

    public void setQuantityInCart(int quantityInCart) {
        this.quantityInCart = quantityInCart;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
