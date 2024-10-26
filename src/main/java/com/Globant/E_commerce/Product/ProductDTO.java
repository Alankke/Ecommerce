package com.Globant.E_commerce.Product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long id;

    @Schema(description = "Name of the product", example = "Wireless Headphones", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Name cannot be null or empty")
    private String name;

    @Schema(description = "Detailed description of the product", example = "Noise-cancelling wireless headphones with 40-hour battery life")
    @NotBlank(message = "Description cannot be null or empty")
    private String description;

    @Schema(description = "Price of the product in USD", example = "199.99", requiredMode = Schema.RequiredMode.REQUIRED)
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private double price;

    @Schema(description = "Type or category of the product", example = "ELECTRONICS", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Type must be provided")
    @Pattern(regexp = "ELECTRONIC|LIBRARY|OTHERS", message = "Type must be either 'ELECTRONIC', 'LIBRARY' or 'OTHERS'")
    private Type type;
}