package com.Globant.E_commerce.Product;

import com.Globant.E_commerce.Product.Decorator.AddOnDecorator;
import com.Globant.E_commerce.Product.Decorator.DiscountDecorator;
import com.Globant.E_commerce.Util.Mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @Operation(summary = "Create a new product", description = "Create a new product using the provided product details.")
    @ApiResponse(responseCode = "201", description = "Product created successfully")
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(
            @RequestBody ProductDTO productDTO) {
        Product product = productService.createProduct(productMapper.convertToEntity(productDTO));
        return new ResponseEntity<>(productMapper.convertToDto(product), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a product by ID", description = "Fetch a product from the database using its ID.")
    @ApiResponse(responseCode = "200", description = "Product retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(
            @Parameter(description = "ID of the product to be retrieved", required = true)
            @PathVariable Long id) {
        Product product = productService.getProductById(id);
        return new ResponseEntity<>(productMapper.convertToDto(product), HttpStatus.OK);
    }

    @Operation(summary = "Update a product", description = "Update an existing product with new details.")
    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(description = "ID of the product to be updated", required = true)
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO) {
        Product updatedProduct = productService.updateProduct(id, productMapper.convertToEntity(productDTO));
        return new ResponseEntity<>(productMapper.convertToDto(updatedProduct), HttpStatus.OK);
    }

    @Operation(summary = "Apply discount to a product", description = "Apply a discount to a product based on its ID and a discount percentage.")
    @ApiResponse(responseCode = "200", description = "Discount applied successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PostMapping("/{id}/apply-discount")
    public ResponseEntity<ProductDTO> applyDiscount(
            @Parameter(description = "ID of the product to apply discount", required = true)
            @PathVariable Long id,
            @Parameter(description = "Discount percentage to be applied", required = true)
            @RequestParam double discountPercentage) {
        Product product = productService.getProductById(id);
        Product discountedProduct = new DiscountDecorator(product, discountPercentage);
        return new ResponseEntity<>(productMapper.convertToDto(discountedProduct), HttpStatus.OK);
    }

    @Operation(summary = "Add extra cost to a product", description = "Add an extra cost and a description of the extra to the product.")
    @ApiResponse(responseCode = "200", description = "Extra cost added successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @PostMapping("/{id}/add-extra")
    public ResponseEntity<ProductDTO> addExtra(
            @Parameter(description = "ID of the product to add extra cost", required = true)
            @PathVariable Long id,
            @Parameter(description = "Extra cost to be added", required = true)
            @RequestParam double extraCost,
            @Parameter(description = "Description of the extra cost", required = true)
            @RequestParam String extraName) {
        Product product = productService.getProductById(id);
        Product productWithExtra = new AddOnDecorator(product, extraCost, extraName);
        return new ResponseEntity<>(productMapper.convertToDto(productWithExtra), HttpStatus.OK);
    }

    @Operation(summary = "Delete a product", description = "Delete a product from the database using its ID.")
    @ApiResponse(responseCode = "200", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Product not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProduct(
            @Parameter(description = "ID of the product to be deleted", required = true)
            @PathVariable Long id) {
        boolean isDeleted = productService.deleteProduct(id);
        return new ResponseEntity<>(isDeleted, isDeleted ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }
}