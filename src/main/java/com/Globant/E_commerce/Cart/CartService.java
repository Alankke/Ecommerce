package com.Globant.E_commerce.Cart;

import com.Globant.E_commerce.CartItem.CartItem;
import com.Globant.E_commerce.Customer.Customer;
import com.Globant.E_commerce.Notification.EmailService;
import com.Globant.E_commerce.Notification.RabbitMQConfiguration;
import com.Globant.E_commerce.Product.Product;
import com.Globant.E_commerce.Product.ProductRepository;
import com.Globant.E_commerce.Util.ExceptionHandler.CartItemNotFoundException;
import com.Globant.E_commerce.Util.ExceptionHandler.CartNotFoundException;
import com.Globant.E_commerce.Util.ExceptionHandler.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CartService implements ICartService {

    @Autowired
    private final CartRepository cartRepository;

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    public CartService(CartRepository cartRepository, ProductRepository productRepository, RabbitTemplate rabbitTemplate, EmailService emailService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.emailService = emailService;
    }
    @Override
    @CachePut(value = "carts", key = "#cart.id")
    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }
    @Override
    @Cacheable(value = "carts", key = "#id")
    public Cart getCartById(Long id) {
        return cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException(id));
    }
    @Override
    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }
    @Override
    @CachePut(value = "carts", key = "#id")
    public Cart updateCart(Long id, Cart updatedCart) {
        Cart existingCart = cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException(id));

        if (updatedCart.getStatus() == Status.SUBMITTED && existingCart.getStatus() != Status.SUBMITTED) {
            Customer customer = existingCart.getCustomer();
            String email = customer.getEmail();
            String message = "Dear " + customer.getName() + ", your cart has been submitted.";

            emailService.sendEmail(email, "Cart Submitted", message);

            rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE, RabbitMQConfiguration.CART_ROUTING_KEY, message);
        }

        existingCart.setStatus(updatedCart.getStatus() != null ? updatedCart.getStatus() : existingCart.getStatus());
        return cartRepository.save(existingCart);
    }


    @Override
    @CacheEvict(value = "carts", key = "#id")
    public boolean deleteCart(Long id) {
        if (!cartRepository.existsById(id)) {
            logger.warn("Cart not found with id: " + id);
            return false;
        }
        cartRepository.deleteById(id);
        return true;
    }
    @CachePut(value = "carts", key = "#cartId")
    public void addProductToCart(Long cartId, Long productId, int quantity) {
        Cart cart = getCartById(cartId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        CartItem existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElse(null);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();
            cart.getCartItems().add(newItem);
        }
        cartRepository.save(cart);
    }
    @CachePut(value = "carts", key = "#cartId")
    public void removeProductFromCart(Long cartId, Long productId) {
        Cart cart = getCartById(cartId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException(cartId, productId));
        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
        } else {
            cart.getCartItems().remove(cartItem);
        }
        cartRepository.save(cart);
    }
    @Cacheable(value = "cartTotals", key = "#cartId")
    public double sumTotal(Long cartId) {
        Cart cart = getCartById(cartId);
        return cart.getCartItems().stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
    @Cacheable(value = "cartProductQuantities", key = "#cartId + '-' + #productId")
    public int currentQuantity(Long cartId, Long productId) {
        Cart cart = getCartById(cartId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return cart.getCartItems().stream()
                .filter(item -> item.getProduct().equals(product))
                .map(CartItem::getQuantity)
                .findFirst()
                .orElse(0);
    }
}