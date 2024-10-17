package com.Globant.E_commerce.Product.Factory;

import com.Globant.E_commerce.Product.Product;
import com.Globant.E_commerce.Product.Type;

public class ProductFactory {

    public static Product createProduct(Long id, String name, String description, double price, Type type) {
        validateProductAttributes(id, name, description, price, type);
        return switch (type) {
            case ELECTRONIC -> new Electronic(id, name, description, price);
            case LIBRARY -> new Library(id, name, description, price);
            case OTHERS -> new Other(id, name, description, price);
        };
    }

    public static void validateProductAttributes(Long id, String name, String description, double price, Type type) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Product description cannot be null or empty");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Product ID must be a positive number");
        }
        if (type == null){
            throw new IllegalArgumentException("Invalid product type");
        }
    }
}