package com.example.najvecaLolcina;


import com.example.najvecaLolcina.entity.*;
import com.example.najvecaLolcina.mapper.CartItemMapper;
import com.example.najvecaLolcina.repository.CartItemRepository;
import com.example.najvecaLolcina.repository.CartRepository;
import com.example.najvecaLolcina.repository.ProductRepository;
import com.example.najvecaLolcina.security.MyyyUserRepo;
import com.example.najvecaLolcina.service.CartService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private MyyyUserRepo myyyUserRepo;
    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private CartService cartService;

    private MyyyUser user;

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
    void addProductToCartException(){

        var orderItem = new OrderItemRequest(1L, 4);
        when(productRepository.findByIdUpdate(orderItem.getProductId())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, ()-> cartService.addProductToCart(orderItem));
        verify(productRepository).findByIdUpdate(1L);
    }

    @Test
    void addProductToCartExceptionTwo(){

        var orderItem = new OrderItemRequest(1L, 11);
        var product = new Product("lol", 10, BigDecimal.valueOf(10), "lol");
        product.setId(1L);

        when(productRepository.findByIdUpdate(1l)).thenReturn(Optional.of(product));

        assertThrows(IllegalArgumentException.class, ()->{cartService.addProductToCart(orderItem);});

        verify(productRepository).findByIdUpdate(1L);
    }


    @Test
    void addProductToCart_createsCartIfUserHasNone() {
        var orderItem = new OrderItemRequest(1L, 5);

        var product = new Product(
                "lol", 10, BigDecimal.valueOf(10), "lol"
        );
        product.setId(1L);

        when(productRepository.findByIdUpdate(1L))
                .thenReturn(Optional.of(product));

        when(myyyUserRepo.findUserForUpdate("testUser"))
                .thenReturn(user);

        when(cartRepository.save(any(Cart.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(cartItemRepository.findByCartAndProductId(any(Cart.class), eq(1L)))
                .thenReturn(Optional.empty());

        cartService.addProductToCart(orderItem);

        assertNotNull(user.getCart());
        verify(cartRepository, times(2)).save(any(Cart.class));
    }

    @Test
    void addProductToCartExceptionThree(){

        var orderItem = new OrderItemRequest(1L, 6);

        var product = new Product(
                "lol", 10, BigDecimal.valueOf(10), "lol"
        );
        product.setId(1L);

        var cart = new Cart();
        var cartItem = new CartItem(5);
        cartItem.setProduct(product);

        user.setCart(cart);

        when(productRepository.findByIdUpdate(1L))
                .thenReturn(Optional.of(product));

        when(myyyUserRepo.findUserForUpdate("testUser"))
                .thenReturn(user);

        when(cartItemRepository.findByCartAndProductId(cart, product.getId()))
                .thenReturn(Optional.of(cartItem));

        assertThrows(IllegalArgumentException.class, ()->{ cartService.addProductToCart(orderItem);});

        verify(cartRepository, never()).save(any(Cart.class));
        assertEquals(5, cartItem.getQuantity());
    }

    @Test
    void addProductToCartFullTest(){

        var orderItem = new OrderItemRequest(1L, 4);

        var product = new Product(
                "lol", 10, BigDecimal.valueOf(10), "lol"
        );
        product.setId(1L);

        var cart = new Cart();
        var cartItem = new CartItem(5);
        cartItem.setProduct(product);
        cart.addcartItem(cartItem);

        user.setCart(cart);

        when(productRepository.findByIdUpdate(1L))
                .thenReturn(Optional.of(product));

        when(myyyUserRepo.findUserForUpdate("testUser"))
                .thenReturn(user);

        when(cartItemRepository.findByCartAndProductId(cart, product.getId()))
                .thenReturn(Optional.of(cartItem));

        when(cartRepository.save(any(Cart.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        cartService.addProductToCart(orderItem);

        ArgumentCaptor<Cart> captor = ArgumentCaptor.forClass(Cart.class);

        verify(cartRepository).save(captor.capture());

        var cartTest = captor.getValue();

        assertEquals(9, cartTest.getCartItemList().get(0).getQuantity());

    }

    @Test
    void returnCartItems_EmptyArray() {
        when(myyyUserRepo.findMyyyUserByName("testUser"))
                .thenReturn(user);

        user.setCart(null);

        var result = cartService.returnCArtItems();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(new ArrayList<>(), result);

        verify(myyyUserRepo).findMyyyUserByName("testUser");
    }

    @Test
    void returnCartItems_Full(){
        when(myyyUserRepo.findMyyyUserByName("testUser"))
                .thenReturn(user);
        var product = new Product(
                "lol", 10, BigDecimal.valueOf(10), "lol"
        );
        product.setId(1L);

        var type = new ProductType("Mouse");

        product.setProductType(type);

        var cart = new Cart();
        var cartItem = new CartItem(5);
        cartItem.setProduct(product);
        cartItem.setId(1L);
        cart.addcartItem(cartItem);


        var cartItemDTO = new CartItemDTO(cartItem.getId(), cartItem.getProduct().getProductType().getType(),
                cartItem.getProduct().getName(), cartItem.getQuantity(),
                BigDecimal.valueOf(cartItem.getQuantity()).multiply(cartItem.getProduct().getPrice()));

        user.setCart(cart);

        when(cartItemMapper.toCArtDTO(cartItem)).thenReturn(cartItemDTO);

        var result = cartService.returnCArtItems();

        assertFalse(result.isEmpty());
        assertEquals(result.getFirst(), cartItemDTO);
        assertEquals(1, result.size());

        verify(cartItemMapper).toCArtDTO(cartItem);
    }


    @Test
    void deleteProductFromCart_Exception(){

        when(myyyUserRepo.findMyyyUserByName("testUser"))
                .thenReturn(user);

        user.setCart(null);

        assertThrows(NoSuchElementException.class, ()-> {cartService.deleteProductFromCart(1L);});

        verify(cartItemRepository, never()).findByCartAndProductId(any(Cart.class), any(Long.class));
        verify(cartItemRepository, never()).delete(any(CartItem.class));

    }

    @Test
    void deleteProductFromCart_ExceptionTwo(){

        when(myyyUserRepo.findMyyyUserByName("testUser"))
                .thenReturn(user);

        var cart = new Cart();
        user.setCart(cart);

        when(cartItemRepository.findByCartAndProductId(cart, 1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, ()->{cartService.deleteProductFromCart(1L);});

        verify(cartItemRepository, never()).delete(any(CartItem.class));
        verify(cartItemRepository).findByCartAndProductId(cart, 1L);
    }

    @Test
    void deleteProductFromCart_Full(){

        when(myyyUserRepo.findMyyyUserByName("testUser"))
                .thenReturn(user);

        var cartItem = new CartItem(5);

        var cart = new Cart();
        user.setCart(cart);

        when(cartItemRepository.findByCartAndProductId(cart, 1L)).thenReturn(Optional.of(cartItem));

        cartService.deleteProductFromCart(1L);

        verify(cartItemRepository).findByCartAndProductId(cart, 1L);

        verify(cartItemRepository).delete(cartItem);
    }


    @Test
    void updateOrDeleteProductFromCart_Exception(){

        when(myyyUserRepo.findMyyyUserByName("testUser"))
                .thenReturn(user);

        user.setCart(null);

        var orderItemReq = new OrderItemRequest(1L, 2);

        assertThrows(NoSuchElementException.class, ()->{cartService.updateOrDeleteProductFromCart(orderItemReq);});

        verify(cartItemRepository, never()).findByCartAndProductId(any(Cart.class), any(Long.class));
        verify(cartItemRepository, never()).delete(any(CartItem.class));
        verify(cartItemRepository, never()).save(any(CartItem.class));
    }


    @Test
    void updateOrDeleteProductFromCart_ExceptionTwo(){

        when(myyyUserRepo.findMyyyUserByName("testUser"))
                .thenReturn(user);

        var cart = new Cart();

        user.setCart(cart);

        var orderItemReq = new OrderItemRequest(1L, 2);

        when(cartItemRepository.findByCartAndProductId(cart, 1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, ()->{cartService.updateOrDeleteProductFromCart(orderItemReq);});
    }

    @Test
    void updateOrDeleteProductFromCart_Test(){

        when(myyyUserRepo.findMyyyUserByName("testUser"))
                .thenReturn(user);

        var cart = new Cart();

        var cartItem = new CartItem(10);

        cart.addcartItem(cartItem);

        user.setCart(cart);

        var orderItemReq = new OrderItemRequest(1L, 5);

        when(cartItemRepository.findByCartAndProductId(cart, 1L)).thenReturn(Optional.of(cartItem));

        cartService.updateOrDeleteProductFromCart(orderItemReq);

        ArgumentCaptor<CartItem> captor = ArgumentCaptor.forClass(CartItem.class);

        verify(cartItemRepository).save(captor.capture());


        var result = captor.getValue();

        assertEquals(5, result.getQuantity());
    }

    @Test()
    void updateOrDeleteProductFromCart_TestTwo(){

        when(myyyUserRepo.findMyyyUserByName("testUser"))
                .thenReturn(user);

        var cart = new Cart();

        var cartItem = new CartItem(5);

        cart.addcartItem(cartItem);

        user.setCart(cart);

        var orderItemReq = new OrderItemRequest(1L, 10);

        when(cartItemRepository.findByCartAndProductId(cart, 1L)).thenReturn(Optional.of(cartItem));

        cartService.updateOrDeleteProductFromCart(orderItemReq);

        verify(cartItemRepository).delete(cartItem);
    }








}
