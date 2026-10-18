package com.example.najvecaLolcina;

import com.example.najvecaLolcina.dto.*;
import com.example.najvecaLolcina.entity.*;
import com.example.najvecaLolcina.enumm.OrderStatus;
import com.example.najvecaLolcina.mapper.CouponMapper;
import com.example.najvecaLolcina.mapper.OrderItemMapper;
import com.example.najvecaLolcina.mapper.OrderMApper;
import com.example.najvecaLolcina.mapper.ShippingAddressMapper;
import com.example.najvecaLolcina.repository.CouponRepository;
import com.example.najvecaLolcina.repository.OrderItemRepository;
import com.example.najvecaLolcina.repository.OrderRepository;
import com.example.najvecaLolcina.repository.ProductRepository;
import com.example.najvecaLolcina.security.MyyyUserRepo;
import com.example.najvecaLolcina.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MyyyUserRepo myyyUserRepo;
    @Mock
    private OrderMApper orderMapper;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private ShippingAddressMapper shippingAddressMapper;
    @Mock
    private CouponMapper couponMapper;

    private  MyyyUser user;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        user = new MyyyUser();
        user.setId(1L);
        user.setName("testUser");

        var authentication = new UsernamePasswordAuthenticationToken(
                "testUser", null, List.of()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void returnOrders_Empty() {

        when(myyyUserRepo.findMyyyUserByName("testUser"))
                .thenReturn(user);

        when(orderRepository.findOrdersByUser(user))
                .thenReturn(List.of());

        var result = orderService.returnOrders();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }


    @Test
    void returnOrders_Full(){

        var order = new Order();
        var orderItem = new OrderItem(BigDecimal.valueOf(10), 5);
        order.addOrderItem(orderItem);

        var orderDTO = new OrderDTO(1L, BigDecimal.valueOf(10), LocalDateTime.now(), OrderStatus.PENDING);

        when(myyyUserRepo.findMyyyUserByName("testUser"))
                .thenReturn(user);

        when(orderRepository.findOrdersByUser(user)).thenReturn(List.of(order));
        when(orderMapper.toOrderDTO(order)).thenReturn(orderDTO);

        var result = orderService.returnOrders();

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getId());
    }


    @Test
    void returnAllOrders_EmptyList(){
        when(orderRepository.findAll()).thenReturn(List.of());

        var result = orderService.returnAllOrders();
        assertTrue(result.isEmpty());
    }

    @Test
    void returnAllOrders_Full(){

        var order = new Order();
        var orderItem = new OrderItem(BigDecimal.valueOf(10), 5);
        order.addOrderItem(orderItem);

        var orderDTO = new OrderDTO(1L, BigDecimal.valueOf(10), LocalDateTime.now(), OrderStatus.PENDING);

        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.toOrderDTO(order)).thenReturn(orderDTO);

        var result = orderService.returnAllOrders();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getId());
    }

    @Test
    void checkProductsFromOneOrder_Exception(){

        when(myyyUserRepo.findMyyyUserByName("testUser"))
                .thenReturn(user);

        when(orderRepository.findOrderByUserAndId(user, 1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, ()->{orderService.checkProductsFromOneOrder(1L);});
    }

        @Test
        void checkProductsFromOneOrder_Full(){

            var order = new Order();

            var orderItem = new OrderItem(BigDecimal.valueOf(10), 5);

            order.getOrderItemList().add(orderItem);

            var orderItemDTO = new OrderItemDTO();
            orderItemDTO.setQunatity(5);
            orderItemDTO.setPrice(BigDecimal.valueOf(10));

            when(myyyUserRepo.findMyyyUserByName("testUser"))
                    .thenReturn(user);

            when(orderRepository.findOrderByUserAndId(user, 1L)).thenReturn(Optional.of(order));
            when(orderItemMapper.toOrderItemDTO(orderItem)).thenReturn(orderItemDTO);

            var result = orderService.checkProductsFromOneOrder(1L);

            assertEquals(1, result.size());
            assertEquals(BigDecimal.valueOf(10), result.getFirst().getPrice());
            assertEquals(5, result.getFirst().getQunatity());
        }


        @Test
        void changeStatusForOrder_Exception(){

        when(orderRepository.findByIdUpdate(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, ()->{orderService.changeStatusForOrder(OrderStatus.DELIVERED, 1L);});

        verify(productRepository, never()).save(any(Product.class));
        }

        @Test
        void changeStatusForOrder_ExceptionTwo(){

        var order = new Order();

        order.setStatus(OrderStatus.PENDING);

            when(orderRepository.findByIdUpdate(1L)).thenReturn(Optional.of(order));
            assertThrows(IllegalArgumentException.class, ()->{orderService.changeStatusForOrder(OrderStatus.PENDING, 1L);});
            verify(productRepository, never()).save(any(Product.class));
        }

        @Test
        void changeStatusForOrder_ExceptionThree(){

            var order = new Order();

            order.setStatus(OrderStatus.DELIVERED);

            when(orderRepository.findByIdUpdate(1L)).thenReturn(Optional.of(order));
            assertThrows(IllegalStateException.class, ()->{orderService.changeStatusForOrder(OrderStatus.CANCELLED, 1L);});
            verify(productRepository, never()).save(any(Product.class));
        }

    @Test
    void changeStatusForOrder_ExceptionFour(){

        var order = new Order();
        var orderDTO = new OrderDTO();
        orderDTO.setStatus(OrderStatus.DELIVERED);

        order.setStatus(OrderStatus.SHIPPED);

        when(orderRepository.findByIdUpdate(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderDTO(order)).thenReturn(orderDTO);
        var result = orderService.changeStatusForOrder(OrderStatus.DELIVERED, 1L);

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);

        verify(productRepository, never()).save(any(Product.class));
        verify(orderRepository).save(captor.capture());

        var cap = captor.getValue();

        assertEquals(cap.getStatus(), OrderStatus.DELIVERED);
        assertEquals(result.getStatus(), OrderStatus.DELIVERED);
    }

    @Test
    void changeStatusForOrder_ShouldRejectSkippedStatus() {

        var order = new Order();
        order.setStatus(OrderStatus.PENDING);

        when(orderRepository.findByIdUpdate(1L))
                .thenReturn(Optional.of(order));

        assertThrows(
                IllegalStateException.class,
                () -> orderService.changeStatusForOrder(
                        OrderStatus.DELIVERED, 1L
                )
        );

        verify(orderRepository, never()).save(any(Order.class));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void changeStatusForOrder_ExceptionFive(){
        var product = new Product(
                "lol", 15, BigDecimal.valueOf(10), "lol"
        );
        product.setId(1L);
        var order = new Order();
        order.setStatus(OrderStatus.PENDING);
        var orderItem = new OrderItem(BigDecimal.valueOf(10), 5);
        var orderDTO = new OrderDTO();
        orderDTO.setStatus(OrderStatus.CANCELLED);
        orderItem.setProduct(product);
        order.addOrderItem(orderItem);


        when(orderRepository.findByIdUpdate(1L)).thenReturn(Optional.of(order));
        when(productRepository.findByIdUpdate(1L)).thenReturn(Optional.of(product));
        when(orderMapper.toOrderDTO(order)).thenReturn(orderDTO);

        var result = orderService.changeStatusForOrder(OrderStatus.CANCELLED, 1L);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(captor.capture());

        var pro = captor.getValue();

        assertEquals(20, pro.getQuantity());
        assertEquals(OrderStatus.CANCELLED, result.getStatus());
    }

    @Test
    void checkCoupon_Exception(){

        when(couponRepository.findCouponByCoupon("coupon")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, ()->{orderService.checkCoupon("coupon");});
    }

    @Test
    void checkCoupon_ExceptionTwo(){

        var coupon = new Coupon();
        coupon.setUses(0);

        when(couponRepository.findCouponByCoupon("coupon")).thenReturn(Optional.of(coupon));

        assertThrows(IllegalStateException.class, ()->{orderService.checkCoupon("coupon");});
    }

    @Test
    void checkCoupon_ExceptionThree(){

        var coupon = new Coupon();
        coupon.setUses(2);
        coupon.setExpiringDate(LocalDateTime.now().minusMinutes(5));

        when(couponRepository.findCouponByCoupon("coupon")).thenReturn(Optional.of(coupon));

        assertThrows(IllegalStateException.class, ()->{orderService.checkCoupon("coupon");});
    }

    @Test
    void checkCoupon_ExceptionFour(){

        var coupon = new Coupon();
        coupon.setUses(2);
        coupon.setExpiringDate(LocalDateTime.now().plusDays(1));

        when(couponRepository.findCouponByCoupon("coupon")).thenReturn(Optional.of(coupon));
        when(myyyUserRepo.findMyyyUserByName("testUser")).thenReturn(user);


        assertThrows(NoSuchElementException.class, ()->{orderService.checkCoupon("coupon");});
    }

    @Test
    void checkCoupon_Full(){

        var cart = new Cart();
        var coupon = new Coupon();
        coupon.setUses(2);
        coupon.setExpiringDate(LocalDateTime.now().plusDays(1));
        user.setCart(cart);

        when(couponRepository.findCouponByCoupon("coupon")).thenReturn(Optional.of(coupon));
        when(myyyUserRepo.findMyyyUserByName("testUser")).thenReturn(user);

        var result = orderService.checkCoupon("coupon");

        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    @Test
    void checkCoupon_FullTwo(){

        var product = new Product(
                "lol", 15, BigDecimal.valueOf(10), "lol"
        );
        product.setId(1L);
        var cartItem = new CartItem(2);
        cartItem.setProduct(product);
        var cart = new Cart();
        cart.addcartItem(cartItem);
        var coupon = new Coupon();
        coupon.setPercentOfDscount(10);
        coupon.setUses(2);
        coupon.setExpiringDate(LocalDateTime.now().plusDays(1));
        user.setCart(cart);

        when(couponRepository.findCouponByCoupon("coupon")).thenReturn(Optional.of(coupon));
        when(myyyUserRepo.findMyyyUserByName("testUser")).thenReturn(user);

        var result = orderService.checkCoupon("coupon");

        assertEquals(0, result.compareTo(BigDecimal.valueOf(18)));
    }

    @Test
    void createCoupon_Exception(){

       var coupon = new Coupon();
       coupon.setCoupon("coupon");

        var couponDTO = new CouponDTO("coupon", 20, 5, LocalDateTime.now().plusDays(2));

        when(couponRepository.findCouponByCoupon("coupon")).thenReturn(Optional.of(coupon));

        assertThrows(IllegalStateException.class, ()->{orderService.createCoupon(couponDTO);});
    }


    @Test
    void createCoupon_Full(){
        var couponDTO = new CouponDTO("coupon", 20, 5, LocalDateTime.now().plusDays(2));
        var coupon = new Coupon(couponDTO.getCoupon(), couponDTO.getPercentOfDscount(), couponDTO.getUses(), couponDTO.getExpiringDate());

        when(couponRepository.findCouponByCoupon("coupon")).thenReturn(Optional.empty());
        when(couponMapper.toCoupon(couponDTO)).thenReturn(coupon);

        ArgumentCaptor<Coupon> captor = ArgumentCaptor.forClass(Coupon.class);

        orderService.createCoupon(couponDTO);

        verify(couponRepository).save(captor.capture());

        var result = captor.getValue();

        assertEquals("coupon", result.getCoupon());
        assertEquals(20, result.getPercentOfDscount());
    }

    @Test
    void createOrder_Exception(){

        var createOrderReq = new CreateOrderRequest();

        List<OrderItemRequest> lista = List.of(new OrderItemRequest(1L, 5));
        createOrderReq.setOrderItemRequestList(lista);

        when(myyyUserRepo.findMyyyUserByName("testUser")).thenReturn(user);
        when(productRepository.findByIdUpdate(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, ()->{orderService.createOrder(createOrderReq, null, null);});

        verify(orderItemRepository, never()).save(any(OrderItem.class));
    }
    @Test
    void createOrder_ExceptionTwo(){

        var product = new Product(
                "lol", 3, BigDecimal.valueOf(10), "lol"
        );
        product.setId(1L);

        var createOrderReq = new CreateOrderRequest();

        List<OrderItemRequest> lista = List.of(new OrderItemRequest(1L, 5));
        createOrderReq.setOrderItemRequestList(lista);

        when(myyyUserRepo.findMyyyUserByName("testUser")).thenReturn(user);
        when(productRepository.findByIdUpdate(1L)).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class, ()->{orderService.createOrder(createOrderReq, null, null);});

        verify(orderItemRepository, never()).save(any(OrderItem.class));
    }


    @Test
    void createOrder_ExceptionThree(){

        var product = new Product(
                "lol", 12, BigDecimal.valueOf(10), "lol"
        );
        product.setId(1L);

        var adress = new ShippingAddress();
        var adressDTO = new ShippingAddressDTO(null, null, null, null, null, null);
        var createOrderReq = new CreateOrderRequest();

        List<OrderItemRequest> lista = List.of(new OrderItemRequest(1L, 5));
        createOrderReq.setOrderItemRequestList(lista);

        when(myyyUserRepo.findMyyyUserByName("testUser")).thenReturn(user);
        when(productRepository.findByIdUpdate(1L)).thenReturn(Optional.of(product));
        when(shippingAddressMapper.toAdress(adressDTO)).thenReturn(adress);
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        orderService.createOrder(createOrderReq, null, adressDTO);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(captor.capture());

        var result = captor.getValue();

        assertEquals(7, result.getQuantity());
    }


    @Test
    void createOrder_ShouldCalculateTotalPriceWithCoupon() {

        var product = new Product(
                "lol", 12, BigDecimal.valueOf(10), "lol"
        );
        product.setId(1L);

        var coupon = new Coupon(
                "coupon", 20, 5, LocalDateTime.now().plusDays(2)
        );

        var shippingAddressDTO = new ShippingAddressDTO(
                "Test", "User", "123456789",
                "test@test.com", "Budva", null
        );

        var address = new ShippingAddress();

        var createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setOrderItemRequestList(
                List.of(new OrderItemRequest(1L, 5))
        );

        when(myyyUserRepo.findMyyyUserByName("testUser"))
                .thenReturn(user);

        when(productRepository.findByIdUpdate(1L))
                .thenReturn(Optional.of(product));

        when(couponRepository.findCouponByCoupon("coupon"))
                .thenReturn(Optional.of(coupon));

        when(shippingAddressMapper.toAdress(shippingAddressDTO))
                .thenReturn(address);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        orderService.createOrder(
                createOrderRequest, "coupon", shippingAddressDTO
        );

        ArgumentCaptor<Order> captor =
                ArgumentCaptor.forClass(Order.class);

        verify(orderRepository, times(2)).save(captor.capture());

        var result = captor.getValue();

        assertEquals(
                0,
                BigDecimal.valueOf(40).compareTo(result.getTotalPrice())
        );

        assertEquals("coupon", result.getAppliedCouponCode());
        assertEquals(4, coupon.getUses());
    }

    @Test
    void checkCoupon_ShouldRoundPriceToTwoDecimals() {

        var product = new Product(
                "Test product",
                5,
                new BigDecimal("12.99"),
                "Description"
        );
        product.setId(1L);

        var cartItem = new CartItem(1);
        cartItem.setProduct(product);

        var cart = new Cart();
        cart.addcartItem(cartItem);

        var coupon = new Coupon(
                "coupon",
                20,
                5,
                LocalDateTime.now().plusDays(2)
        );

        user.setCart(cart);

        when(couponRepository.findCouponByCoupon("coupon"))
                .thenReturn(Optional.of(coupon));

        when(myyyUserRepo.findMyyyUserByName("testUser"))
                .thenReturn(user);

        var result = orderService.checkCoupon("coupon");

        assertEquals(new BigDecimal("10.39"), result);
    }



}
