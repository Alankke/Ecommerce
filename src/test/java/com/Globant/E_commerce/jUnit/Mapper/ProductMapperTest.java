package com.Globant.E_commerce.jUnit.Mapper;

import com.Globant.E_commerce.Product.Product;
import com.Globant.E_commerce.Product.ProductDTO;
import com.Globant.E_commerce.Product.Type;
import com.Globant.E_commerce.Util.Mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
    }

    @Test
    void convertToEntityTest() {
        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .name("Laptop")
                .description("High-performance laptop")
                .price(999.99)
                .type(Type.ELECTRONIC)
                .build();

        Product result = productMapper.convertToEntity(productDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Laptop", result.getName());
        assertEquals("High-performance laptop", result.getDescription());
        assertEquals(999.99, result.getPrice());
        assertEquals(Type.ELECTRONIC, result.getType());
    }

    @Test
    void convertToEntityNullValuesTest() {
        
        ProductDTO productDTO = ProductDTO.builder()
                .id(1L)
                .build();

        Product result = productMapper.convertToEntity(productDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNull(result.getName());
        assertNull(result.getDescription());
        assertEquals(0.0, result.getPrice());
        assertNull(result.getType());
    }

    @Test
    void convertToDtoTest() {
        Product product = Product.builder()
                .id(2L)
                .name("Smartphone")
                .description("Latest model smartphone")
                .price(799.99)
                .type(Type.ELECTRONIC)
                .build();

        ProductDTO result = productMapper.convertToDto(product);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals("Smartphone", result.getName());
        assertEquals("Latest model smartphone", result.getDescription());
        assertEquals(799.99, result.getPrice());
        assertEquals(Type.ELECTRONIC, result.getType());
    }

        @Test
        void convertToDtoNullValuesTest() {
            Product product = Product.builder()
                    .id(2L)
                    .build();

            ProductDTO result = productMapper.convertToDto(product);

            assertNotNull(result);
            assertEquals(2L, result.getId());
            assertNull(result.getName());
            assertNull(result.getDescription());
            assertEquals(0.0, result.getPrice());
            assertNull(result.getType());
        }

    @Test
    void roundTripMappingTest() {
        Product originalProduct = Product.builder()
                .id(3L)
                .name("Headphones")
                .description("Noise-cancelling headphones")
                .price(249.99)
                .type(Type.ELECTRONIC)
                .build();

        ProductDTO dto = productMapper.convertToDto(originalProduct);
        Product resultProduct = productMapper.convertToEntity(dto);

        assertNotNull(resultProduct);
        assertEquals(originalProduct.getId(), resultProduct.getId());
        assertEquals(originalProduct.getName(), resultProduct.getName());
        assertEquals(originalProduct.getDescription(), resultProduct.getDescription());
        assertEquals(originalProduct.getPrice(), resultProduct.getPrice());
        assertEquals(originalProduct.getType(), resultProduct.getType());
    }
}
