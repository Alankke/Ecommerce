package com.Globant.E_commerce.Util.ExceptionHandler;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(Long cartId, Long productId) {
        super("Cart item not found with cartId: " + cartId + "and productId: " + productId);
    }
}
