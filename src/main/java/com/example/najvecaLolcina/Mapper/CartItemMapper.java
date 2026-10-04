package com.example.najvecaLolcina.Mapper;

import com.example.najvecaLolcina.Entity.CartItem;
import com.example.najvecaLolcina.Entity.CartItemDTO;
import com.example.najvecaLolcina.OrderItemRequest;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {

    public CartItemDTO toCArtDTO(CartItem cartItem){
        return new CartItemDTO(cartItem.getId(), cartItem.getProduct().getProductType().getType(),
                cartItem.getProduct().getName(), cartItem.getQuantity(), cartItem.getQuantity()*cartItem.getProduct().getPrice());
    }

    public OrderItemRequest toOrderItemRequest(CartItem cartItem){
        return new OrderItemRequest(cartItem.getProduct().getId(), cartItem.getQuantity());
    }


}
