package com.Globant.E_commerce.Util.ExceptionHandler;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(Long id) {
        super("Cart not found with id: " + id);
    }
}