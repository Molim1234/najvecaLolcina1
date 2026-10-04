package com.example.najvecaLolcina;

public class OrderItemRequest {

    private Long productId;
    private int quantity;

    public Long getProductId() {
        return productId;
    }

    public OrderItemRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
