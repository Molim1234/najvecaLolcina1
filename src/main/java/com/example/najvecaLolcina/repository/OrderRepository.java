package com.example.najvecaLolcina.repository;

import com.example.najvecaLolcina.entity.MyyyUser;
import com.example.najvecaLolcina.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    public List<Order> findOrdersByUser(MyyyUser myyyUser);

    @Query("select o from Order o where o.user=:myyyUser and o.id=:id")
    public Optional<Order> findOrderByUserAndId(MyyyUser myyyUser, Long id);

}
