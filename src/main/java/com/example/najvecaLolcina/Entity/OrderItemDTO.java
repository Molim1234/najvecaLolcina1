package com.example.najvecaLolcina.Entity;


public class OrderItemDTO {

    private Long id;
    private String productType;
    private String productName;
    private double price;
    private int qunatity;
    private double totalPrice;

    public OrderItemDTO(Long id, String productType, String productName, double price, int qunatity, double totalPrice) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQunatity() {
        return qunatity;
    }

    public void setQunatity(int qunatity) {
        this.qunatity = qunatity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
