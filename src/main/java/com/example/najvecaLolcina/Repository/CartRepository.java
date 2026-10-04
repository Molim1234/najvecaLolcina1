package com.example.najvecaLolcina.Repository;

import com.example.najvecaLolcina.Entity.Cart;
import com.example.najvecaLolcina.Entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
