package com.example.najvecaLolcina.service;

import com.example.najvecaLolcina.dto.CreateOrderRequest;
import com.example.najvecaLolcina.dto.CouponDTO;
import com.example.najvecaLolcina.dto.OrderDTO;
import com.example.najvecaLolcina.dto.OrderItemDTO;
import com.example.najvecaLolcina.dto.ShippingAddressDTO;
import com.example.najvecaLolcina.entity.*;
import com.example.najvecaLolcina.mapper.CouponMapper;
import com.example.najvecaLolcina.mapper.OrderItemMapper;
import com.example.najvecaLolcina.mapper.ShippingAddressMapper;
import com.example.najvecaLolcina.dto.OrderItemRequest;
import com.example.najvecaLolcina.mapper.OrderMApper;
import com.example.najvecaLolcina.enumm.OrderStatus;
import com.example.najvecaLolcina.repository.CouponRepository;
import com.example.najvecaLolcina.repository.OrderItemRepository;
import com.example.najvecaLolcina.repository.OrderRepository;
import com.example.najvecaLolcina.repository.ProductRepository;
import com.example.najvecaLolcina.security.MyyyUserRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final MyyyUserRepo myyyUserRepo;
    private final OrderMApper orderMapper;
    private final CartService cartService;
    private final OrderItemMapper orderItemMapper;
    private final CouponRepository couponRepository;
    private final ShippingAddressMapper shippingAddressMapper;
    private final CouponMapper couponMapper;

    private OrderStatus[] statuses = {OrderStatus.PENDING,
            OrderStatus.CONFIRMED,
            OrderStatus.SHIPPED,
            OrderStatus.DELIVERED};


    public OrderService(OrderItemRepository orderItemRepository, OrderRepository orderRepository, ProductRepository productRepository, MyyyUserRepo myyyUserRepo, OrderMApper orderMapper, CartService cartService, OrderItemMapper orderItemMapper, CouponRepository couponRepository, ShippingAddressMapper shippingAddressMapper, CouponMapper couponMapper) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.myyyUserRepo = myyyUserRepo;
        this.orderMapper = orderMapper;
        this.cartService = cartService;
        this.orderItemMapper = orderItemMapper;
        this.couponRepository = couponRepository;
        this.shippingAddressMapper = shippingAddressMapper;
        this.couponMapper = couponMapper;
    }

    @Transactional
    public OrderDTO createOrderFromCart(String coupon, ShippingAddressDTO shippingAddressDTO){
        var listOfItems = cartService.returnItemsForOrder();
        var orderDTO = createOrder(listOfItems, coupon, shippingAddressDTO);
        cartService.cleanCartAfterOrder();
        return orderDTO;
    }


    @Transactional
    public OrderDTO createOrder(@Valid CreateOrderRequest createOrderRequest, String coupon, ShippingAddressDTO shippingAddressDTO){

         var lista = createOrderRequest.getOrderItemRequestList();

        BigDecimal totalprice= new BigDecimal(0);
        Order or = new Order();
        or.setStatus(OrderStatus.PENDING);
        or.setDateTime(LocalDateTime.now());
        orderRepository.save(or);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userr = authentication.getName();
        MyyyUser authenticatedUser = myyyUserRepo
                .findMyyyUserByName(userr);
        or.setUser(authenticatedUser);

         for(OrderItemRequest order:lista){

             Product product = productRepository.findByIdUpdate(order.getProductId()).orElseThrow(()->new NoSuchElementException("No product with id "+order.getProductId()));
             if(product.getQuantity()< order.getQuantity()){
                 throw new IllegalArgumentException("Not enough amount");
             }
             product.setQuantity(product.getQuantity()- order.getQuantity());

             OrderItem orderItem = new OrderItem(product.getPrice(), order.getQuantity());

             orderItem.setProduct(product);
             orderItemRepository.save(orderItem);
             or.addOrderItem(orderItem);
             productRepository.save(product);



            totalprice = totalprice.add(product.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())));
         }
         if(coupon!=null) {
             var checkedCoupon = couponRepository.findCouponByCoupon(coupon).orElseThrow(()->new NoSuchElementException("Coupon is not valid"));

             if(checkedCoupon.getUses()<=0 || checkedCoupon.getExpiringDate().isBefore(LocalDateTime.now())){
                 throw new NoSuchElementException("Coupon expired");
             }
             checkedCoupon.setUses(checkedCoupon.getUses()-1);

             totalprice = calculatePriceAfterDiscount(
                     totalprice,
                     checkedCoupon.getPercentOfDscount()
             );

             or.setAppliedCouponCode(checkedCoupon.getCoupon());
             couponRepository.save(checkedCoupon);
         }
        totalprice = totalprice.setScale(
                2,
                RoundingMode.HALF_UP
        );

        or.setTotalPrice(totalprice);

         or.setTotalPrice(totalprice);
         or.setShippingAddress(shippingAddressMapper.toAdress(shippingAddressDTO));

         return orderMapper.toOrderDTO(orderRepository.save(or));
    }


    public List<OrderDTO> returnOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userr = authentication.getName();
        MyyyUser authenticatedUser = myyyUserRepo
                .findMyyyUserByName(userr);
        return orderRepository.findOrdersByUser(authenticatedUser).stream().map(orderMapper::toOrderDTO).toList();
    }

    public List<OrderDTO> returnAllOrders() {
        return orderRepository.findAll().stream().map(orderMapper::toOrderDTO).toList();
    }

    public List<OrderItemDTO> checkProductsFromOneOrder(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userr = authentication.getName();
        MyyyUser authenticatedUser = myyyUserRepo
                .findMyyyUserByName(userr);

        Optional<Order> ordertest = orderRepository.findOrderByUserAndId(authenticatedUser, id);
        if(ordertest.isEmpty()){
            throw new NoSuchElementException("There is no order with this id and user");
        }
        var order = ordertest.get();
        return order.getOrderItemList().stream().map(orderItemMapper::toOrderItemDTO).toList();
    }
    @Transactional
    public OrderDTO changeStatusForOrder(OrderStatus newStatus, Long id) {

//                PENDING,
//                CONFIRMED,
//                SHIPPED,
//                DELIVERED,
//                CANCELLED

        var order = orderRepository.findByIdUpdate(id)
                .orElseThrow(() ->
                        new NoSuchElementException("There is no order with this id"));

        OrderStatus oldStatus = order.getStatus();

        if (oldStatus.equals(newStatus) || oldStatus.equals(OrderStatus.CANCELLED)) {
            throw new IllegalArgumentException("This order is already with this status, or order was cancelled before");
        }
        if(!newStatus.equals(OrderStatus.CANCELLED) && !newStatus.equals(OrderStatus.DELIVERED)){
            for(int i=0;i<statuses.length;i++){
                if(statuses[i].equals(oldStatus) && !statuses[i+1].equals(newStatus)){
                    throw new IllegalStateException("You cant put this status for this order");
                }
            }
        }

        if (newStatus.equals(OrderStatus.CANCELLED)) {

            if (oldStatus != OrderStatus.PENDING &&
                    oldStatus != OrderStatus.CONFIRMED) {

                throw new IllegalStateException(
                        "This order cannot be cancelled from its current status"
                );
            }

            for (OrderItem item : order.getOrderItemList()) {
                var product = productRepository.findByIdUpdate(item.getProduct().getId()).orElseThrow(()->new NoSuchElementException("There is no product with this id"));

                product.setQuantity(
                        product.getQuantity() + item.getQuantity()
                );

                productRepository.save(product);
            }
        }

        order.setStatus(newStatus);
        orderRepository.save(order);

        return orderMapper.toOrderDTO(order);
    }

    @Transactional
    public BigDecimal checkCoupon(String coupon) {

    var checkedCoupon = couponRepository.findCouponByCoupon(coupon).orElseThrow(()-> new NoSuchElementException("This coupon doesnt exist"));

    if(checkedCoupon.getUses()<=0 || checkedCoupon.getExpiringDate().isBefore(LocalDateTime.now())){
        throw new IllegalStateException("Coupon expired");
    }
        BigDecimal price= new BigDecimal(0);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userr = authentication.getName();
        MyyyUser authenticatedUser = myyyUserRepo
                .findMyyyUserByName(userr);

        var cart = authenticatedUser.getCart();
        if(cart==null){
            throw new NoSuchElementException("Person doesnt have items in cart");
        }
        for(CartItem item: cart.getCartItemList()){
            var product = item.getProduct();

            price = price.add(product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));


        }

        return calculatePriceAfterDiscount(
                price,
                checkedCoupon.getPercentOfDscount()
        );
    }

    @Transactional
    public void createCoupon(@Valid CouponDTO couponDTO) {

        if(couponRepository.findCouponByCoupon(couponDTO.getCoupon()).isPresent()){
            throw new IllegalStateException("This coupon already exist");
        }

        couponRepository.save(couponMapper.toCoupon(couponDTO));
    }

    private BigDecimal calculatePriceAfterDiscount(
            BigDecimal totalPrice,
            int discountPercent
    ) {
        return totalPrice
                .multiply(BigDecimal.valueOf(100L - discountPercent))
                .divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP
                );
    }

}
