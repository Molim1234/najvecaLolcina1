package com.example.najvecaLolcina.Entity;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private MyyyUser myyyUser;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItemList = new ArrayList<>();

    public void addcartItem(CartItem cartItem){
        cartItemList.add(cartItem);
        cartItem.setCart(this);
    }

    public Cart() {
    }

    public MyyyUser getMyyyUser() {
        return myyyUser;
    }

    public void setMyyyUser(MyyyUser myyyUser) {
        this.myyyUser = myyyUser;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


}
