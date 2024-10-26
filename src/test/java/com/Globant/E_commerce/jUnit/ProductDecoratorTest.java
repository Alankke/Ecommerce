package com.Globant.E_commerce.jUnit;

import com.Globant.E_commerce.Product.Decorator.AddOnDecorator;
import com.Globant.E_commerce.Product.Decorator.DiscountDecorator;
import com.Globant.E_commerce.Product.Product;
import com.Globant.E_commerce.Product.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class ProductDecoratorTest {

    @Mock
    private Product mockProduct;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("AddOn extra to product")
    @Test
    void addOnDecoratorTest() {
        when(mockProduct.getName()).thenReturn("Laptop Asus");
        when(mockProduct.getDescription()).thenReturn("Asus Rog");
        when(mockProduct.getPrice()).thenReturn(200.00);
        when(mockProduct.getType()).thenReturn(Type.ELECTRONIC);
        when(mockProduct.getId()).thenReturn(1L);

        Product decoratedProduct = new AddOnDecorator(mockProduct, 60.00, "More Ram");

        Assertions.assertEquals(260.00, decoratedProduct.getPrice(), 0.001);
        Assertions.assertEquals("Asus Rog More Ram", decoratedProduct.getDescription());
    }

    @DisplayName("Discount to product")
    @Test
    void discountDecoratorTest() {
        when(mockProduct.getName()).thenReturn("Laptop Asus");
        when(mockProduct.getDescription()).thenReturn("Asus Rog");
        when(mockProduct.getPrice()).thenReturn(200.00);
        when(mockProduct.getType()).thenReturn(Type.ELECTRONIC);
        when(mockProduct.getId()).thenReturn(1L);

        Product discountedProduct = new DiscountDecorator(mockProduct, 20);

        double expectedPrice = 200.00 - (200.00 * 0.20);

        Assertions.assertEquals(expectedPrice, discountedProduct.getPrice());
    }
}
