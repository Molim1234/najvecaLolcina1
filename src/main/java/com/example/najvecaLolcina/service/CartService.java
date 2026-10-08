package com.example.najvecaLolcina.service;

import com.example.najvecaLolcina.CreateOrderRequest;
import com.example.najvecaLolcina.entity.Cart;
import com.example.najvecaLolcina.entity.CartItemDTO;
import com.example.najvecaLolcina.entity.CartItem;
import com.example.najvecaLolcina.entity.MyyyUser;
import com.example.najvecaLolcina.mapper.CartItemMapper;
import com.example.najvecaLolcina.OrderItemRequest;
import com.example.najvecaLolcina.repository.CartItemRepository;
import com.example.najvecaLolcina.repository.CartRepository;
import com.example.najvecaLolcina.repository.ProductRepository;
import com.example.najvecaLolcina.security.MyyyUserRepo;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CartService {

    private CartItemRepository cartItemRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private MyyyUserRepo myyyUserRepo;
    private CartItemMapper cartItemMapper;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository, CartRepository cartRepository, MyyyUserRepo myyyUserRepo, CartItemMapper cartItemMapper) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.myyyUserRepo = myyyUserRepo;
        this.cartItemMapper = cartItemMapper;
    }

    @Transactional
    public void addProductToCart(OrderItemRequest orderItemRequest) {

        var product = productRepository.findByIdUpdate(orderItemRequest.getProductId()).orElseThrow(()->new NoSuchElementException("No product with id "+orderItemRequest.getProductId()));
        if(product.getQuantity()<orderItemRequest.getQuantity())
            throw new NoSuchElementException("Not enough");

        var authenticatedUser = returnAuthenticatedUser();

        Cart cart = authenticatedUser.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setMyyyUser(authenticatedUser);
            cart = cartRepository.save(cart);
            authenticatedUser.setCart(cart);
        }

        var cartItem = new CartItem(orderItemRequest.getQuantity());
        Optional<CartItem> optionalCartItem = cartItemRepository.findByCartAndProductId(cart, product.getId());
        if(optionalCartItem.isEmpty()) {
            authenticatedUser.getCart().addcartItem(cartItem);
            cartItem.setProduct(product);

        }else{
            CartItem item = optionalCartItem.get();
            int number = item.getQuantity()+ orderItemRequest.getQuantity();
            if(number> product.getQuantity()){
                throw new NoSuchElementException("Not enough quantity of product");
            }
            item.setQuantity(number);
            cartItem = item;
        }

        cartRepository.save(cart);

    }
@Transactional
    public List<CartItemDTO> returnCArtItems() {

    var authenticatedUser = returnAuthenticatedUser();

        if(authenticatedUser.getCart()==null){
            return new ArrayList<>();
        }else{
            return authenticatedUser.getCart().getCartItemList().stream().map(cartItemMapper::toCArtDTO).toList();
        }
    }

    @Transactional
    public void deleteProductFromCart(Long id) {
        var authenticatedUser = returnAuthenticatedUser();

        var cart = authenticatedUser.getCart();
        if(cart==null){
            throw new NoSuchElementException("Person doesnt have a cart");
        }
        var cartItemForRemoving = cartItemRepository.findByCartAndProductId(cart, id).orElseThrow(()-> new NoSuchElementException("This user doesnt have this item in his cart with id "+id));
        cartItemRepository.delete(cartItemForRemoving);
    }

@Transactional
    public void updateOrDeleteProductFromCart(OrderItemRequest orderItemRequest) {
    var authenticatedUser = returnAuthenticatedUser();

        var cart = authenticatedUser.getCart();
        if(cart==null){
            throw new NoSuchElementException("Person doesnt have a cart");
        }
        var cartItemForUpdateOrRemove = cartItemRepository.findByCartAndProductId(cart, orderItemRequest.getProductId()).orElseThrow(()-> new NoSuchElementException("This user doesnt have this item in his cart with id "+orderItemRequest.getProductId()));

        if(orderItemRequest.getQuantity()>=cartItemForUpdateOrRemove.getQuantity()){
            cartItemRepository.delete(cartItemForUpdateOrRemove);
        }else{
            cartItemForUpdateOrRemove.setQuantity(cartItemForUpdateOrRemove.getQuantity()- orderItemRequest.getQuantity());
            cartItemRepository.save(cartItemForUpdateOrRemove);
        }


    }

    @Transactional
    public void updateOneOrDeleteProduct(Long productId) {

        var authenticatedUser = returnAuthenticatedUser();

        var cart = authenticatedUser.getCart();
        if(cart==null){
            throw new NoSuchElementException("Person doesnt have a cart");
        }
        var cartItemForUpdateOrRemove = cartItemRepository.findByCartAndProductId(cart, productId).orElseThrow(()-> new NoSuchElementException("This user doesnt have this item in his cart with id "+productId));
        if(cartItemForUpdateOrRemove.getQuantity()==1){
            cartItemRepository.delete(cartItemForUpdateOrRemove);
        }else{
            cartItemForUpdateOrRemove.setQuantity(cartItemForUpdateOrRemove.getQuantity()-1);
            cartItemRepository.save(cartItemForUpdateOrRemove);
        }

    }

    @Transactional
    public void plusOneProductFromCart(Long productId) {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        var authenticatedUser = myyyUserRepo.findUserForUpdate(username);

        var cart = authenticatedUser.getCart();
        if(cart==null){
            throw new NoSuchElementException("Person doesnt have a cart");
        }
        var cartItemForUpdateOrRemove = cartItemRepository.findByCartAndProductId(cart, productId).orElseThrow(()-> new NoSuchElementException("This user doesnt have this item in his cart with id "+productId));

        var product = productRepository.findByIdUpdate(
                cartItemForUpdateOrRemove.getProduct().getId()
        ).orElseThrow(() -> new NoSuchElementException(
                "There is no product with this id"
        ));

        if(cartItemForUpdateOrRemove.getQuantity()>=product.getQuantity()){
            throw new NoSuchElementException("Product doesnt have enough quantity");
        }
        cartItemForUpdateOrRemove.setQuantity(cartItemForUpdateOrRemove.getQuantity()+1);
        cartItemRepository.save(cartItemForUpdateOrRemove);
    }

    public CreateOrderRequest returnItemsForOrder(){

        var authenticatedUser = returnAuthenticatedUser();

        var cart = cartRepository.findByUserForUpdate(authenticatedUser)
                .orElseThrow(() ->
                        new NoSuchElementException("Cart doesn't exist"));

        var items = cartItemRepository.findAllCartItems(cart);
        if(items.isEmpty()){
            throw new NoSuchElementException("Cart doesnt have any items");
        }
        List<OrderItemRequest> orderItemRequestList = items.stream().map(cartItemMapper::toOrderItemRequest).toList();
        var orderrequest = new CreateOrderRequest();
        orderrequest.setOrderItemRequestList(orderItemRequestList);
        return orderrequest;
    }
@Transactional
    public void cleanCartAfterOrder(){

        var authenticatedUser = returnAuthenticatedUser();

        var cart = authenticatedUser.getCart();

        cart.getCartItemList().clear();
        cartRepository.save(cart);
    }


    public MyyyUser returnAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userr = authentication.getName();
        return myyyUserRepo.findMyyyUserByName(userr);
    }


}
