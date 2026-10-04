package com.example.najvecaLolcina.Controller;


import com.example.najvecaLolcina.Entity.*;
import com.example.najvecaLolcina.OrderStatus;
import com.example.najvecaLolcina.Service.OrderService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public OrderDTO orderItems(@RequestParam(required = false) String coupon, @RequestBody @Valid ShippingAddressDTO shippingAddressDTO){
       return orderService.createOrderFromCart(coupon, shippingAddressDTO);
    }

    @GetMapping("/orders/my-orders")
    public List<OrderDTO> returnOrders(){
        return orderService.returnOrders();
    }

    @PostMapping("/coupons/apply")
    public double checkCoupon(@RequestBody String coupon){
        return orderService.checkCoupon(coupon);
    }

    @GetMapping("/orders/{id}")
    public List<OrderItemDTO> checkProductsFromOneOrder(@PathVariable Long id){
        return orderService.checkProductsFromOneOrder(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/orders")
    public List<Order> allOrders(){
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
    @PostMapping("/coupon/add")
    public void addCoupon(@RequestBody @Valid CouponDTO couponDTO){
        orderService.createCoupon(couponDTO);
    }


}
