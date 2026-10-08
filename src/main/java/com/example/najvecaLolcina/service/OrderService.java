package com.example.najvecaLolcina.service;

import com.example.najvecaLolcina.CreateOrderRequest;
import com.example.najvecaLolcina.entity.*;
import com.example.najvecaLolcina.mapper.CouponMapper;
import com.example.najvecaLolcina.mapper.OrderItemMapper;
import com.example.najvecaLolcina.mapper.ShippingAddressMapper;
import com.example.najvecaLolcina.OrderItemRequest;
import com.example.najvecaLolcina.mapper.OrderMApper;
import com.example.najvecaLolcina.OrderStatus;
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
import java.lang.invoke.WrongMethodTypeException;
import java.math.BigDecimal;
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
    private CartService cartService;
    private OrderItemMapper orderItemMapper;
    private CouponRepository couponRepository;
    private ShippingAddressMapper shippingAddressMapper;
    private CouponMapper couponMapper;


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
    public OrderDTO createOrder(CreateOrderRequest createOrderRequest, String coupon, ShippingAddressDTO shippingAddressDTO){

         var lista = createOrderRequest.getOrderItemRequestList();

         if(lista.isEmpty())
             throw new NoSuchElementException();

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
             if(product.getQuantity()< order.getQuantity() || order.getQuantity()<=0){
                 throw new WrongMethodTypeException("No amount");
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


             totalprice = totalprice.subtract(
                     totalprice
                             .multiply(BigDecimal.valueOf(checkedCoupon.getPercentOfDscount()))
                             .divide(BigDecimal.valueOf(100))
             );
             or.setAppliedCouponCode(checkedCoupon.getCoupon());
             couponRepository.save(checkedCoupon);
         }

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

        var order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("There is no order with this id"));

        OrderStatus oldStatus = order.getStatus();

        if (oldStatus.equals(newStatus)) {
            throw new NoSuchElementException("This order is already with this status");
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
        throw new NoSuchElementException("Coupon expired");
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

        return price.subtract(price.multiply(BigDecimal.valueOf(checkedCoupon.getPercentOfDscount()).divide(BigDecimal.valueOf(100))));
    }

    public void createCoupon(@Valid CouponDTO couponDTO) {

        if(couponRepository.findCouponByCoupon(couponDTO.getCoupon()).isPresent()){
            throw new IllegalStateException("This coupon already exist");
        }

        couponRepository.save(couponMapper.toCoupon(couponDTO));
    }
}
