package com.example.najvecaLolcina.Repository;

import com.example.najvecaLolcina.Entity.MyyyUser;
import com.example.najvecaLolcina.Entity.OrderItem;
import com.example.najvecaLolcina.Entity.Product;
import com.example.najvecaLolcina.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select o from OrderItem o where o.product=:p and o.order in " +
            "(select orr from Order orr where orr.user=:myyyUser and orr.status=:status)")
    public Optional<OrderItem> findOrderItemMethod(Product p, MyyyUser myyyUser, OrderStatus status);

}
