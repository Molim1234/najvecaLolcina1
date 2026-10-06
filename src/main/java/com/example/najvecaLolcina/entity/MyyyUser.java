package com.example.najvecaLolcina.entity;


import com.example.najvecaLolcina.security.role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "useroplja")
public class MyyyUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name can not be blank")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "password can not be blank")
    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Order> orders= new ArrayList<>();

    @OneToOne(mappedBy = "myyyUser")
    private Cart cart;


    public MyyyUser(Long id, String name, String password, role role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public MyyyUser() {
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public role getRole() {
        return role;
    }

    public void setRole(role role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
