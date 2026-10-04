package com.example.najvecaLolcina;

import java.util.List;

public class CreateOrderRequest {

private List<OrderItemRequest> orderItemRequestList;

    public List<OrderItemRequest> getOrderItemRequestList() {
        return orderItemRequestList;
    }

    public void setOrderItemRequestList(List<OrderItemRequest> orderItemRequestList) {
        this.orderItemRequestList = orderItemRequestList;
    }
}
