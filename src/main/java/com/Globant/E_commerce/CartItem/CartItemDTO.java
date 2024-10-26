package com.Globant.E_commerce.CartItem;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemDTO {

    @Schema(description = "Unique identifier for the product", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @Schema(description = "Name of the product in the cart", example = "Wireless Headphones", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Product name cannot be null or empty")
    private String productName;

    @Schema(description = "Price of the product in USD", example = "199.99", requiredMode = Schema.RequiredMode.REQUIRED)
    @DecimalMin(value = "0.0", inclusive = false, message = "Product price must be greater than 0")
    private double productPrice;

    @Schema(description = "Quantity of the product in the cart", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}