package com.Globant.E_commerce.Util.Mapper;

import com.Globant.E_commerce.Product.Product;
import com.Globant.E_commerce.Product.ProductDTO;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product convertToEntity(ProductDTO productDTO) {
        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .type(productDTO.getType())
                .build();
    }

    public ProductDTO convertToDto(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .type(product.getType())
                .build();
    }
}