package com.Globant.E_commerce.Product.Decorator;
import com.Globant.E_commerce.Product.Product;

public abstract class ProductDecorator extends Product {
    protected final Product product;

    public ProductDecorator(Product product) {
        super(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getType());
        this.product = product;
    }

    @Override
    public double getPrice() {
        return product.getPrice();
    }
}
