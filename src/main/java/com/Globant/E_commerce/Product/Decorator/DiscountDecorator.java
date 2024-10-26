package com.Globant.E_commerce.Product.Decorator;
import com.Globant.E_commerce.Product.Product;

public class DiscountDecorator extends ProductDecorator {
    private final double discountPercentage;

    public DiscountDecorator(Product product, double discountPercentage) {
        super(product);
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double getPrice() {
        return product.getPrice() * (1 - discountPercentage / 100);
    }
}
