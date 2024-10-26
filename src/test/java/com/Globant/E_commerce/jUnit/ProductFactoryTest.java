package com.Globant.E_commerce.jUnit;

import com.Globant.E_commerce.Product.Factory.ProductFactory;
import com.Globant.E_commerce.Product.Product;
import com.Globant.E_commerce.Product.Type;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductFactoryTest {

    @DisplayName("Invalid product attributes test")
    @ParameterizedTest
    @MethodSource("invalidProductAttributes")
    void createProductInvalidAttributes(Long id, String name, String description, double price, Type type, Class<? extends Exception> expectedException, String expectedMessage) {
        Exception exception = assertThrows(expectedException, () ->
                ProductFactory.createProduct(id, name, description, price, type)
        );
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @DisplayName("Validate product attributes")
    @ParameterizedTest
    @MethodSource("invalidProductAttributes")
    void validateProductAttributesInvalidTest(Long id, String name, String description, double price, Type type, Class<? extends Exception> expectedException, String expectedMessage) {
        Exception exception = assertThrows(expectedException, () ->
                ProductFactory.validateProductAttributes(id, name, description, price, type)
        );
        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @DisplayName("Create product for each type with valid attributes")
    @Test
    void createProductEachType() {
        Product iPhone15 = ProductFactory.createProduct(1L, "iPhone 15 Pro Max", "Apple smartphone number 15", 1150.00, Type.ELECTRONIC);
        Product cleanCode = ProductFactory.createProduct(2L, "Clean Code", "A Handbook of Agile Software Craftsmanship", 100.00, Type.LIBRARY);
        Product fernet = ProductFactory.createProduct(3L, "Fernet", "1 liter bottle of fernet", 20.00, Type.OTHERS);

        Assertions.assertEquals(Type.ELECTRONIC, iPhone15.getType());
        Assertions.assertEquals(Type.LIBRARY, cleanCode.getType());
        Assertions.assertEquals(Type.OTHERS, fernet.getType());
    }

    private static Stream<Arguments> invalidProductAttributes() {
        return Stream.of(
                Arguments.of(1L, null, "Description", 100.0, Type.ELECTRONIC, IllegalArgumentException.class, "Product name cannot be null or empty"),
                Arguments.of(1L, "", "Description", 100.0, Type.ELECTRONIC, IllegalArgumentException.class, "Product name cannot be null or empty"),
                Arguments.of(1L, "  ", "Description", 100.0, Type.ELECTRONIC, IllegalArgumentException.class, "Product name cannot be null or empty"),
                Arguments.of(1L, "Name", null, 100.0, Type.ELECTRONIC, IllegalArgumentException.class, "Product description cannot be null or empty"),
                Arguments.of(1L, "Name", "", 100.0, Type.ELECTRONIC, IllegalArgumentException.class, "Product description cannot be null or empty"),
                Arguments.of(1L, "Name", "  ", 100.0, Type.ELECTRONIC, IllegalArgumentException.class, "Product description cannot be null or empty"),
                Arguments.of(1L, "Name", "Description", 0.0, Type.ELECTRONIC, IllegalArgumentException.class, "Product price must be greater than zero"),
                Arguments.of(1L, "Name", "Description", -10.0, Type.ELECTRONIC, IllegalArgumentException.class, "Product price must be greater than zero"),
                Arguments.of(null, "Name", "Description", 100.0, Type.ELECTRONIC, IllegalArgumentException.class, "Product ID must be a positive number"),
                Arguments.of(0L, "Name", "Description", 100.0, Type.ELECTRONIC, IllegalArgumentException.class, "Product ID must be a positive number"),
                Arguments.of(-1L, "Name", "Description", 100.0, Type.ELECTRONIC, IllegalArgumentException.class, "Product ID must be a positive number"),
                Arguments.of(1L, "Name", "Description", 100.0, null, IllegalArgumentException.class, "Invalid product type")
        );
    }
}