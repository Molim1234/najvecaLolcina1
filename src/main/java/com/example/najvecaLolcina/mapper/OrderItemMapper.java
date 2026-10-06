package com.example.najvecaLolcina.mapper;

import com.example.najvecaLolcina.entity.OrderItem;
import com.example.najvecaLolcina.entity.OrderItemDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderItemMapper {

    public OrderItemDTO toOrderItemDTO(OrderItem orderItem){
        return new OrderItemDTO(orderItem.getId(), orderItem.getProduct().getProductType().getType(),
                orderItem.getProduct().getName(), orderItem.getPrice(), orderItem.getQuantity(),
                orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                );
    }

}
