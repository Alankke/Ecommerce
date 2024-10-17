package com.Globant.E_commerce.Cart;

import com.Globant.E_commerce.CartItem.CartItemDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {

    @Schema(description = "Unique identifier for the cart", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID of the customer who owns the cart", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @Schema(description = "Status of the cart, either 'DRAFT' or 'SUBMITTED'", example = "DRAFT", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Status cannot be null or empty")
    @Pattern(regexp = "DRAFT|SUBMITTED", message = "Status must be either 'DRAFT' or 'SUBMITTED'")
    private String status;

    @Schema(description = "List of items in the cart", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Cart items cannot be null")
    @NotEmpty(message = "Cart must contain at least one item")
    private List<CartItemDTO> cartItems;
}