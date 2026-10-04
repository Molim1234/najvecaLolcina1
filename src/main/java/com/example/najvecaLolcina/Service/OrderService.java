package com.example.najvecaLolcina.Service;

import com.example.najvecaLolcina.CreateOrderRequest;
import com.example.najvecaLolcina.Entity.*;
import com.example.najvecaLolcina.Mapper.CouponMapper;
import com.example.najvecaLolcina.Mapper.OrderItemMapper;
import com.example.najvecaLolcina.Mapper.ShippingAddressMapper;
import com.example.najvecaLolcina.OrderItemRequest;
import com.example.najvecaLolcina.Mapper.OrderMApper;
import com.example.najvecaLolcina.OrderStatus;
import com.example.najvecaLolcina.Repository.CouponRepository;
import com.example.najvecaLolcina.Repository.OrderItemRepository;
import com.example.najvecaLolcina.Repository.OrderRepository;
import com.example.najvecaLolcina.Repository.ProductRepository;
import com.example.najvecaLolcina.Security.MyyyUserRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.lang.invoke.WrongMethodTypeException;
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

        double totalprice=0;
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

             Product product = productRepository.findById(order.getProductId()).orElseThrow(()->new NoSuchElementException("No product with id "+order.getProductId()));
             if(product.getQuantity()< order.getQuantity() || order.getQuantity()<=0){
                 throw new WrongMethodTypeException("No amount");
             }
             product.setQuantity(product.getQuantity()- order.getQuantity());

             OrderItem orderItem = new OrderItem(product.getPrice(), order.getQuantity());

             orderItem.setProduct(product);
             orderItemRepository.save(orderItem);
             or.addOrderItem(orderItem);
             productRepository.save(product);

             totalprice+=product.getPrice()* order.getQuantity();
         }
         if(coupon!=null) {
             var checkedCoupon = couponRepository.findCouponByCoupon(coupon).orElseThrow(()->new NoSuchElementException("Coupon is not valid"));

             if(checkedCoupon.getUses()<=0 || checkedCoupon.getExpiringDate().isBefore(LocalDateTime.now())){
                 throw new NoSuchElementException("Coupon expired");
             }
             checkedCoupon.setUses(checkedCoupon.getUses()-1);
             totalprice=totalprice-(totalprice*checkedCoupon.getPercentOfDscount()/100);
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

    public List<Order> returnAllOrders() {
        return orderRepository.findAll();
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
        var order = orderRepository.findById(id).orElseThrow(()->new NoSuchElementException("There is no order with this id"));
        if(order.getStatus().equals(newStatus))
            throw new NoSuchElementException("This order is already with this status");

        order.setStatus(newStatus);
        if(newStatus.equals(OrderStatus.CANCELLED)) {
            for(OrderItem items:order.getOrderItemList()){
                var product = items.getProduct();
                product.setQuantity(product.getQuantity()+ items.getQuantity());
                productRepository.save(product);
            }
        }
        orderRepository.save(order);
        return orderMapper.toOrderDTO(order);
    }

    public double checkCoupon(String coupon) {

    var checkedCoupon = couponRepository.findCouponByCoupon(coupon).orElseThrow(()-> new NoSuchElementException("This coupon doesnt exist"));

    if(checkedCoupon.getUses()<=0 || checkedCoupon.getExpiringDate().isBefore(LocalDateTime.now())){
        throw new NoSuchElementException("Coupon expired");
    }
        double price=0;

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
            price+=product.getPrice()*item.getQuantity();
        }

        return price-(price*checkedCoupon.getPercentOfDscount()/100);
    }

    public void createCoupon(@Valid CouponDTO couponDTO) {

        if(couponRepository.findCouponByCoupon(couponDTO.getCoupon()).isPresent()){
            throw new IllegalStateException("This coupon already exist");
        }

        couponRepository.save(couponMapper.toCoupon(couponDTO));
    }
}
