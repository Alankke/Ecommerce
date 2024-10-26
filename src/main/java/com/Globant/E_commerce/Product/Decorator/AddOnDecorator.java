package com.Globant.E_commerce.Product.Decorator;

import com.Globant.E_commerce.Product.Product;

public class AddOnDecorator extends ProductDecorator{
    private final double addOnCost;
    private final String addOnName;

    public AddOnDecorator(Product product, double addOnCost, String addOnName) {
        super(product);
        this.addOnCost = addOnCost;
        this.addOnName = addOnName;
    }

    @Override
    public String getDescription() {
        return product.getDescription() + " " + addOnName;
    }

    @Override
    public double getPrice() {
        return product.getPrice() + addOnCost;
    }
}
