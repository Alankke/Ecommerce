package com.Globant.E_commerce.Product;

import com.Globant.E_commerce.Util.ExceptionHandler.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    @CachePut(value = "products", key = "#product.id")
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    @CachePut(value = "products", key = "#id")
    public Product updateProduct(Long id, Product updatedProduct) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(updatedProduct.getName() != null ? updatedProduct.getName() : existingProduct.getName());
                    existingProduct.setDescription(updatedProduct.getDescription() != null ? updatedProduct.getDescription() : existingProduct.getDescription());
                    existingProduct.setPrice(updatedProduct.getPrice() > 0 ? updatedProduct.getPrice() : existingProduct.getPrice());
                    existingProduct.setType(updatedProduct.getType() != null ? updatedProduct.getType() : existingProduct.getType());
                    return productRepository.save(existingProduct);
                })
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public boolean deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepository.deleteById(id);
        return true;
    }
}