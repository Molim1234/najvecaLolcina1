package com.example.najvecaLolcina.Controller;


import com.example.najvecaLolcina.Entity.CartItemDTO;
import com.example.najvecaLolcina.OrderItemRequest;
import com.example.najvecaLolcina.Service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/cart/add")
    public void addToCart(@RequestBody OrderItemRequest orderItemRequest){
        cartService.addProductToCart(orderItemRequest);
    }

    @GetMapping("/cart/view")
    public List<CartItemDTO> viewCart(){
        return cartService.returnCArtItems();
    }

    @DeleteMapping("/cart/delete/product/{id}")
    public void deleteProductFromCart(@PathVariable Long id){
        cartService.deleteProductFromCart(id);
    }

    @PatchMapping("/cart/update/product")
    public void updateOrDeleteProductFromCart(@RequestBody OrderItemRequest orderItemRequest){
        cartService.updateOrDeleteProductFromCart(orderItemRequest);
    }

    @PatchMapping("/cart/update/minusOne/{productId}")
    public void updateOneOrDeleteProductFromCart(@PathVariable Long productId){
        cartService.updateOneOrDeleteProduct(productId);
    }

    @PatchMapping("/cart/update/plusOne/{productId}")
    public void updatePlusOneProductFromCart(@PathVariable Long productId){
        cartService.plusOneProductFromCart(productId);
    }

}
