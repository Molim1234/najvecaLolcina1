package com.example.najvecaLolcina.Mapper;

import com.example.najvecaLolcina.Entity.OrderItem;
import com.example.najvecaLolcina.Entity.OrderItemDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    public OrderItemDTO toOrderItemDTO(OrderItem orderItem){
        return new OrderItemDTO(orderItem.getId(), orderItem.getProduct().getProductType().getType(),
                orderItem.getProduct().getName(), orderItem.getProduct().getPrice(), orderItem.getQuantity(),
                orderItem.getPrice()* orderItem.getQuantity()
                );
    }

}
