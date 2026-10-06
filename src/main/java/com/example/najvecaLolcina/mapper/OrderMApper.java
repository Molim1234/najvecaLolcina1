package com.example.najvecaLolcina.mapper;

import com.example.najvecaLolcina.entity.Order;
import com.example.najvecaLolcina.entity.OrderDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderMApper {

    public OrderDTO toOrderDTO(Order order){
        return new OrderDTO(order.getId(),order.getTotalPrice(), order.getDateTime(), order.getStatus());
    }


}
