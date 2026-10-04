package com.example.najvecaLolcina.Mapper;

import com.example.najvecaLolcina.Entity.Order;
import com.example.najvecaLolcina.Entity.OrderDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderMApper {

    public OrderDTO toOrderDTO(Order order){
        return new OrderDTO(order.getId(),order.getTotalPrice(), order.getDateTime(), order.getStatus());
    }


}
