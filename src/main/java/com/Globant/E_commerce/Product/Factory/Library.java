package com.Globant.E_commerce.Product.Factory;

import com.Globant.E_commerce.Product.Product;
import com.Globant.E_commerce.Product.Type;

public class Library extends Product {
    public Library(Long id, String name, String description, double price) {
        super(id, name, description, price, Type.LIBRARY);
    }
}
