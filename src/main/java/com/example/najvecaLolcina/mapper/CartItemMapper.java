package com.example.najvecaLolcina.mapper;

import com.example.najvecaLolcina.entity.CartItem;
import com.example.najvecaLolcina.entity.CartItemDTO;
import com.example.najvecaLolcina.OrderItemRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CartItemMapper {

    public CartItemDTO toCArtDTO(CartItem cartItem){
        return new CartItemDTO(cartItem.getId(), cartItem.getProduct().getProductType().getType(),
                cartItem.getProduct().getName(), cartItem.getQuantity(),
        BigDecimal.valueOf(cartItem.getQuantity()).multiply(cartItem.getProduct().getPrice()));
    }

    public OrderItemRequest toOrderItemRequest(CartItem cartItem){
        return new OrderItemRequest(cartItem.getProduct().getId(), cartItem.getQuantity());
    }


}
