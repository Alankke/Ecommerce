package com.Globant.E_commerce.Util.Mapper;

import com.Globant.E_commerce.Cart.Cart;
import com.Globant.E_commerce.Cart.CartDTO;
import com.Globant.E_commerce.Cart.Status;
import com.Globant.E_commerce.CartItem.CartItemDTO;
import com.Globant.E_commerce.Customer.Customer;
import com.Globant.E_commerce.Customer.CustomerRepository;
import com.Globant.E_commerce.Util.ExceptionHandler.CustomerNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartDTO entityToDto(Cart cart) {
        List<CartItemDTO> cartItems = cart.getCartItems() != null
                ? cart.getCartItems().stream().map(item -> {
            CartItemDTO dto = new CartItemDTO();
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setProductPrice(item.getProduct().getPrice());
            dto.setQuantity(item.getQuantity());
            return dto;
        }).collect(Collectors.toList())
                : new ArrayList<>();

        return CartDTO.builder()
                .id(cart.getId())
                .customerId(cart.getCustomer().getId())
                .status(cart.getStatus().name())
                .cartItems(cartItems)
                .build();
    }

    public Cart dtoToEntity(CartDTO cartDTO, CustomerRepository customerRepository) {
        Customer customer = customerRepository.findById(cartDTO.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(cartDTO.getCustomerId()));

        return Cart.builder()
                .id(cartDTO.getId())
                .customer(customer)
                .status(Status.valueOf(cartDTO.getStatus()))
                .build();
    }
}