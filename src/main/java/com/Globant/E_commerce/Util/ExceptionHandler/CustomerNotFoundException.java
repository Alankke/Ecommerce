package com.Globant.E_commerce.Util.ExceptionHandler;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long id) {
        super("Customer not found with id: " + id);
    }
}