package com.example.najvecaLolcina.repository;

import com.example.najvecaLolcina.entity.Cart;
import com.example.najvecaLolcina.entity.CartItem;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from CartItem c where c.cart = :cart and c.product.id = :productId")
    Optional<CartItem> findByCartAndProductId(@Param("cart") Cart cart, @Param("productId") Long productId);

    @Query("select c from CartItem c where c.cart=:cart")
    List<CartItem> findAllCartItems(@Param("cart") Cart cart);


}
