package com.Globant.E_commerce.Product;

import java.util.List;

public interface IProductService {
    Product createProduct(Product product);
    Product getProductById(Long id);
    Product updateProduct(Long id, Product product);
    List<Product> getAllProducts();
    boolean deleteProduct(Long id);
}
