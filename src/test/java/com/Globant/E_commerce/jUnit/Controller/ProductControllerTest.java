package com.Globant.E_commerce.jUnit.Controller;

import com.Globant.E_commerce.Product.*;
import com.Globant.E_commerce.Util.ExceptionHandler.GlobalExceptionHandler;
import com.Globant.E_commerce.Util.ExceptionHandler.ProductNotFoundException;
import com.Globant.E_commerce.Util.Mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class ProductControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    private Product productTest;
    private ProductDTO productDTOTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        productTest = new Product(1L, "Laptop", "High end laptop", 1500.0, Type.ELECTRONIC);
        productDTOTest = new ProductDTO(1L, "Laptop", "High end laptop", 1500.0, Type.ELECTRONIC);
    }

    @DisplayName("Create product test")
    @Test
    void createProductTest() throws Exception {
        when(productMapper.convertToEntity(any(ProductDTO.class))).thenReturn(productTest);
        when(productService.createProduct(any(Product.class))).thenReturn(productTest);
        when(productMapper.convertToDto(any(Product.class))).thenReturn(productDTOTest);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"name\": \"Laptop\", \"description\": \"High end laptop\", \"price\": 1500.0, \"type\": \"ELECTRONIC\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.description").value("High end laptop"))
                .andExpect(jsonPath("$.price").value(1500.0))
                .andExpect(jsonPath("$.type").value("ELECTRONIC"));

        verify(productService, times(1)).createProduct(any(Product.class));
    }

    @DisplayName("Get product test")
    @Test
    void getProductByIdTest() throws Exception {
        when(productService.getProductById(1L)).thenReturn(productTest);
        when(productMapper.convertToDto(any(Product.class))).thenReturn(productDTOTest);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.description").value("High end laptop"))
                .andExpect(jsonPath("$.price").value(1500.0))
                .andExpect(jsonPath("$.type").value("ELECTRONIC"));

        verify(productService, times(1)).getProductById(1L);
    }

    @DisplayName("Update product test")
    @Test
    void updateProductTest() throws Exception {
        Product updatedProduct = new Product(1L, "Updated Laptop", "Updated description", 2000.0, Type.ELECTRONIC);

        when(productMapper.convertToEntity(any(ProductDTO.class))).thenReturn(updatedProduct);
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);
        when(productMapper.convertToDto(any(Product.class))).thenReturn(productDTOTest);


        productDTOTest.setName("Updated Laptop");
        productDTOTest.setDescription("Updated description");
        productDTOTest.setPrice(2000.0);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"name\": \"Updated Laptop\", \"description\": \"Updated description\", \"price\": 2000.0, \"type\": \"ELECTRONIC\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.price").value(2000.0))
                .andExpect(jsonPath("$.type").value("ELECTRONIC"));

        verify(productService, times(1)).updateProduct(eq(1L), any(Product.class));
    }

    @DisplayName("Delete product test")
    @Test
    void deleteProductTest() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(productService, times(1)).deleteProduct(1L);
    }


    @DisplayName("Try to get a non existent product")
    @Test
    void getProductByIdNotFoundTest() throws Exception {
        when(productService.getProductById(1L)).thenThrow(new ProductNotFoundException(1L));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isNotFound());

        verify(productService, times(1)).getProductById(1L);
    }
}