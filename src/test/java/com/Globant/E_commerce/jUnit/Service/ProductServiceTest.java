package com.Globant.E_commerce.jUnit.Service;

import com.Globant.E_commerce.Product.Product;
import com.Globant.E_commerce.Product.ProductRepository;
import com.Globant.E_commerce.Product.ProductService;
import com.Globant.E_commerce.Product.Type;
import com.Globant.E_commerce.Util.ExceptionHandler.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product productTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productTest = new Product(1L, "Laptop", "High-end gaming laptop", 1500.0, Type.ELECTRONIC);
        when(productRepository.save(any(Product.class))).thenReturn(productTest);
    }

    @DisplayName("Create product test")
    @Test
    void createProductTest() {
        when(productRepository.save(any(Product.class))).thenReturn(productTest);

        Product createdProduct = productService.createProduct(productTest);

        assertNotNull(createdProduct);
        assertEquals(productTest.getId(), createdProduct.getId());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @DisplayName("Get product by id test")
    @Test
    void getProductByIdTest() {
        when(productRepository.findById(productTest.getId())).thenReturn(Optional.of(productTest));

        Product foundProduct = productService.getProductById(productTest.getId());

        assertNotNull(foundProduct);
        assertEquals(productTest.getId(), foundProduct.getId());
        verify(productRepository, times(1)).findById(productTest.getId());
    }

    @DisplayName("Get non existent product by id test")
    @Test
    void getNonExistentProductByIdTest() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.getProductById(999L));

        assertEquals("Product not found with id: 999", exception.getMessage());
    }

    @DisplayName("Get all products test")
    @Test
    void getAllProductsTest() {
        List<Product> products = Collections.singletonList(productTest);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> allProducts = productService.getAllProducts();

        assertNotNull(allProducts);
        assertEquals(1, allProducts.size());
        verify(productRepository, times(1)).findAll();
    }

    @DisplayName("Update existing product (all fields changed)")
    @Test
    void updateProductAllFieldsTest() {
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Name");
        updatedProduct.setDescription("Updated Description");
        updatedProduct.setPrice(2000.0);
        updatedProduct.setType(Type.ELECTRONIC);

        when(productRepository.findById(productTest.getId())).thenReturn(Optional.of(productTest));
        when(productRepository.save(any(Product.class))).thenReturn(productTest);

        Product result = productService.updateProduct(productTest.getId(), updatedProduct);

        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(2000.0, result.getPrice());
        assertEquals(Type.ELECTRONIC, result.getType());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @DisplayName("Update existing product (some fields null)")
    @Test
    void updateProductSomeFieldsNullTest() {
        Product updatedProduct = new Product();
        updatedProduct.setName(null);
        updatedProduct.setDescription(null);
        updatedProduct.setPrice(-1);
        updatedProduct.setType(null);

        when(productRepository.findById(productTest.getId())).thenReturn(Optional.of(productTest));
        when(productRepository.save(any(Product.class))).thenReturn(productTest);

        Product result = productService.updateProduct(productTest.getId(), updatedProduct);

        assertEquals(productTest.getName(), result.getName());
        assertEquals(productTest.getDescription(), result.getDescription());
        assertEquals(productTest.getPrice(), result.getPrice());
        assertEquals(productTest.getType(), result.getType());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @DisplayName("Update existing product (no changes)")
    @Test
    void updateProductNoChangesTest() {
        Product updatedProduct = new Product();
        updatedProduct.setName(productTest.getName());
        updatedProduct.setDescription(productTest.getDescription());
        updatedProduct.setPrice(productTest.getPrice());
        updatedProduct.setType(productTest.getType());

        when(productRepository.findById(productTest.getId())).thenReturn(Optional.of(productTest));
        when(productRepository.save(any(Product.class))).thenReturn(productTest);

        Product result = productService.updateProduct(productTest.getId(), updatedProduct);

        assertEquals(productTest.getName(), result.getName());
        assertEquals(productTest.getDescription(), result.getDescription());
        assertEquals(productTest.getPrice(), result.getPrice());
        assertEquals(productTest.getType(), result.getType());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @DisplayName("Update non existent product test")
    @Test
    void updateNonExistentProductTest() {
        Product updatedProduct = new Product();
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(999L, updatedProduct));
        verify(productRepository, never()).save(any(Product.class));
    }

    @DisplayName("Delete existing product test")
    @Test
    void deleteProductTest() {
        when(productRepository.existsById(productTest.getId())).thenReturn(true);
        doNothing().when(productRepository).deleteById(productTest.getId());

        boolean isDeleted = productService.deleteProduct(productTest.getId());

        assertTrue(isDeleted);
        verify(productRepository, times(1)).deleteById(productTest.getId());
    }

    @DisplayName("Try to delete non existent product")
    @Test
    void deleteNonExistentProductTest() {
        when(productRepository.existsById(999L)).thenReturn(false);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(999L));

        assertEquals("Product not found with id: 999", exception.getMessage());
        verify(productRepository, times(0)).deleteById(999L);
    }
}