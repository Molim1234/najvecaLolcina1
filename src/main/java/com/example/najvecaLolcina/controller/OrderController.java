package com.example.najvecaLolcina.controller;


import com.example.najvecaLolcina.entity.*;
import com.example.najvecaLolcina.OrderStatus;
import com.example.najvecaLolcina.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public OrderDTO orderItems(@RequestParam(required = false) String coupon, @RequestBody @Valid ShippingAddressDTO shippingAddressDTO){
       return orderService.createOrderFromCart(coupon, shippingAddressDTO);
    }

    @GetMapping("/orders")
    public List<OrderDTO> returnOrders(){
        return orderService.returnOrders();
    }

    @PostMapping("/coupons")
    public BigDecimal checkCoupon(@RequestBody String coupon){
        return orderService.checkCoupon(coupon);
    }

    @GetMapping("/orders/{id}")
    public List<OrderItemDTO> checkProductsFromOneOrder(@PathVariable Long id){
        return orderService.checkProductsFromOneOrder(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/orders")
    public List<OrderDTO> allOrders(){
        return orderService.returnAllOrders();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/orders/{id}/status")
    public OrderDTO changeStatusForOrder(@RequestParam("status")OrderStatus newStatus,
                                         @PathVariable Long id
                                         ){
        return orderService.changeStatusForOrder(newStatus, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/coupons")
    public void addCoupon(@RequestBody @Valid CouponDTO couponDTO){
        orderService.createCoupon(couponDTO);
    }


}
