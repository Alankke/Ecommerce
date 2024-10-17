package com.Globant.E_commerce.Cart;

import com.Globant.E_commerce.Customer.CustomerRepository;
import com.Globant.E_commerce.Util.Mapper.CartMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;
    private final CustomerRepository customerRepository;

    @Autowired
    public CartController(CartService cartService, CartMapper cartMapper, CustomerRepository customerRepository) {
        this.cartService = cartService;
        this.cartMapper = cartMapper;
        this.customerRepository = customerRepository;
    }

    @Operation(summary = "Create a new cart", description = "Create a new cart for a customer with the provided details.")
    @ApiResponse(responseCode = "201", description = "Cart created successfully")
    @PostMapping
    public ResponseEntity<CartDTO> createCart(@Valid @RequestBody CartDTO cartDTO) {
        Cart cart = cartService.createCart(cartMapper.dtoToEntity(cartDTO, customerRepository));
        return new ResponseEntity<>(cartMapper.entityToDto(cart), HttpStatus.CREATED);
    }

    @Operation(summary = "Get cart by ID", description = "Retrieve the details of a specific cart by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart found"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCartById(
            @Parameter(description = "ID of the cart to retrieve", required = true)
            @PathVariable Long id) {
        Cart cart = cartService.getCartById(id);
        return new ResponseEntity<>(cartMapper.entityToDto(cart), HttpStatus.OK);
    }

    @Operation(summary = "Get all carts", description = "Retrieve a list of all carts.")
    @ApiResponse(responseCode = "200", description = "Carts retrieved successfully")
    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<Cart> carts = cartService.getAllCarts();
        List<CartDTO> cartDTOs = carts.stream().map(cartMapper::entityToDto).collect(Collectors.toList());
        return new ResponseEntity<>(cartDTOs, HttpStatus.OK);
    }

    @Operation(summary = "Update a cart", description = "Update the details of an existing cart by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart updated successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCart(
            @Parameter(description = "ID of the cart to update", required = true)
            @PathVariable Long id,
            @RequestBody CartDTO cartDTO) {
        cartService.updateCart(id, cartMapper.dtoToEntity(cartDTO, customerRepository));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Delete a cart", description = "Delete a cart by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cart deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Cart not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(
            @Parameter(description = "ID of the cart to delete", required = true)
            @PathVariable Long id) {
        cartService.deleteCart(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Add product to cart", description = "Add a product to a specific cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product added to cart successfully"),
            @ApiResponse(responseCode = "404", description = "Cart or product not found")
    })
    @PostMapping("/{cartId}/products/{productId}")
    public ResponseEntity<Void> addCartItemToCart(
            @Parameter(description = "ID of the cart", required = true)
            @PathVariable Long cartId,
            @Parameter(description = "ID of the product to add", required = true)
            @PathVariable Long productId,
            @RequestParam int quantity) {
        cartService.addProductToCart(cartId, productId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Remove product from cart", description = "Remove a product from a specific cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed from cart successfully"),
            @ApiResponse(responseCode = "404", description = "Cart or product not found")
    })
    @DeleteMapping("/{cartId}/products/{productId}")
    public ResponseEntity<Void> removeCartItem(
            @Parameter(description = "ID of the cart", required = true)
            @PathVariable Long cartId,
            @Parameter(description = "ID of the product to remove", required = true)
            @PathVariable Long productId) {
        cartService.removeProductFromCart(cartId, productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Get cart total", description = "Calculate and return the total cost of the cart.")
    @ApiResponse(responseCode = "200", description = "Total calculated successfully")
    @GetMapping("/{cartId}/total")
    public ResponseEntity<Double> getCartTotal(
            @Parameter(description = "ID of the cart", required = true)
            @PathVariable Long cartId) {
        double total = cartService.sumTotal(cartId);
        return new ResponseEntity<>(total, HttpStatus.OK);
    }

    @Operation(summary = "Get product quantity in cart", description = "Retrieve the quantity of a specific product in a cart.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quantity retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Cart or product not found")
    })
    @GetMapping("/{cartId}/products/{productId}/quantity")
    public ResponseEntity<Integer> getCartItemQuantity(
            @Parameter(description = "ID of the cart", required = true)
            @PathVariable Long cartId,
            @Parameter(description = "ID of the product", required = true)
            @PathVariable Long productId) {
        int quantity = cartService.currentQuantity(cartId, productId);
        return new ResponseEntity<>(quantity, HttpStatus.OK);
    }
}