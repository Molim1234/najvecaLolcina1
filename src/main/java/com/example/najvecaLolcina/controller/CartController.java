package com.example.najvecaLolcina.controller;


import com.example.najvecaLolcina.entity.CartItemDTO;
import com.example.najvecaLolcina.OrderItemRequest;
import com.example.najvecaLolcina.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart/items")
    public void addToCart(@RequestBody @Valid OrderItemRequest orderItemRequest){
        cartService.addProductToCart(orderItemRequest);
    }

    @GetMapping("/cart")
    public List<CartItemDTO> viewCart(){
        return cartService.returnCArtItems();
    }

    @DeleteMapping("/cart/items/{id}")
    public void deleteProductFromCart(@PathVariable Long id){
        cartService.deleteProductFromCart(id);
    }

    @PatchMapping("/cart/items")
    public void updateOrDeleteProductFromCart(@RequestBody @Valid OrderItemRequest orderItemRequest){
        cartService.updateOrDeleteProductFromCart(orderItemRequest);
    }

    @PatchMapping("/cart/items/minusOne/{productId}")
    public void updateOneOrDeleteProductFromCart(@PathVariable Long productId){
        cartService.updateOneOrDeleteProduct(productId);
    }

    @PatchMapping("/cart/items/plusOne/{productId}")
    public void updatePlusOneProductFromCart(@PathVariable Long productId){
        cartService.plusOneProductFromCart(productId);
    }

}
