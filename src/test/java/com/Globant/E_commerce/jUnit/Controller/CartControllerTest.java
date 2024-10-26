package com.Globant.E_commerce.jUnit.Controller;

import com.Globant.E_commerce.Cart.*;
import com.Globant.E_commerce.Customer.Customer;
import com.Globant.E_commerce.Customer.CustomerRepository;
import com.Globant.E_commerce.Util.ExceptionHandler.CartItemNotFoundException;
import com.Globant.E_commerce.Util.ExceptionHandler.CartNotFoundException;
import com.Globant.E_commerce.Util.ExceptionHandler.GlobalExceptionHandler;
import com.Globant.E_commerce.Util.Mapper.CartMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private CartMapper cartMapper;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CartController cartController;

    private MockMvc mockMvc;

    private Cart cartTest;
    private CartDTO cartDTOTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(cartController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        cartTest = new Cart(1L, new Customer(), Status.DRAFT, new ArrayList<>());
        cartDTOTest = new CartDTO(1L, 1L, "DRAFT", new ArrayList<>());
    }

    @DisplayName("Create a cart test")
    @Test
    void createCartTest() throws Exception {
        when(cartMapper.dtoToEntity(any(CartDTO.class), any(CustomerRepository.class))).thenReturn(cartTest);
        when(cartService.createCart(any(Cart.class))).thenReturn(cartTest);
        when(cartMapper.entityToDto(any(Cart.class))).thenReturn(cartDTOTest);

        mockMvc.perform(post("/api/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"customerId\": 1, \"status\": \"DRAFT\", \"cartItems\": [{ \"productId\": 101, \"quantity\": 2 }] }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.status").value("DRAFT"));

        verify(cartService, times(1)).createCart(any(Cart.class));
    }

    @DisplayName("Get carts test")
    @Test
    void getCartByIdTest() throws Exception {
        when(cartService.getCartById(1L)).thenReturn(cartTest);
        when(cartMapper.entityToDto(any(Cart.class))).thenReturn(cartDTOTest);

        mockMvc.perform(get("/api/carts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.status").value("DRAFT"));

        verify(cartService, times(1)).getCartById(1L);
    }

    @DisplayName("Get all carts test")
    @Test
    void getAllCartsTest() throws Exception {
        List<Cart> carts = Arrays.asList(cartTest, cartTest);
        when(cartService.getAllCarts()).thenReturn(carts);
        when(cartMapper.entityToDto(any(Cart.class))).thenReturn(cartDTOTest);

        mockMvc.perform(get("/api/carts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(1));

        verify(cartService, times(1)).getAllCarts();
    }

    @DisplayName("Update cart test")
    @Test
    void updateCartTest() throws Exception {
        Cart updatedCart = new Cart(1L, new Customer(), Status.SUBMITTED, new ArrayList<>());
        when(cartMapper.dtoToEntity(any(CartDTO.class), any(CustomerRepository.class))).thenReturn(updatedCart);
        when(cartService.updateCart(eq(1L), any(Cart.class))).thenReturn(updatedCart);

        mockMvc.perform(put("/api/carts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"customerId\": 1, \"status\": \"SUBMITTED\" }"))
                .andExpect(status().isNoContent());

        verify(cartService, times(1)).updateCart(eq(1L), any(Cart.class));
    }

    @DisplayName("Update status of cart")
    @Test
    void updateCartStatusTest() throws Exception {
        Cart updatedCart = new Cart(1L, new Customer(), Status.SUBMITTED, new ArrayList<>());
        when(cartMapper.dtoToEntity(any(CartDTO.class), any(CustomerRepository.class))).thenReturn(updatedCart);
        when(cartService.updateCart(eq(1L), any(Cart.class))).thenReturn(updatedCart);

        mockMvc.perform(put("/api/carts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"customerId\": 1, \"status\": \"SUBMITTED\" }"))
                .andExpect(status().isNoContent());

        verify(cartService, times(1)).updateCart(eq(1L), any(Cart.class));
        verify(cartMapper).dtoToEntity(argThat(dto -> "SUBMITTED".equals(dto.getStatus())), any(CustomerRepository.class));
    }

    @DisplayName("Delete a cart")
    @Test
    void deleteCartTest() throws Exception {
        when(cartService.deleteCart(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/carts/1"))
                .andExpect(status().isNoContent());

        verify(cartService, times(1)).deleteCart(1L);
    }

    @DisplayName("Try to get a non existent cart")
    @Test
    void getCartByIdNotFoundTest() throws Exception {
        when(cartService.getCartById(1L)).thenThrow(new CartNotFoundException(1L));

        mockMvc.perform(get("/api/carts/1"))
                .andExpect(status().isNotFound());

        verify(cartService, times(1)).getCartById(1L);
    }

    @DisplayName("Try to create a cart with invalid status")
    @Test
    void createCartInvalidStatusTest() throws Exception {
        mockMvc.perform(post("/api/carts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"customerId\": 1, \"status\": \"INVALID_STATUS\", \"cartItems\": [] }"))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Try to update a non existent cart")
    @Test
    void updateCartNotFoundTest() throws Exception {
        when(cartMapper.dtoToEntity(any(CartDTO.class), any(CustomerRepository.class))).thenReturn(cartTest);
        doThrow(new CartNotFoundException(1L)).when(cartService).updateCart(eq(1L), any(Cart.class));

        mockMvc.perform(put("/api/carts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": 1, \"customerId\": 1, \"status\": \"SUBMITTED\" }"))
                .andExpect(status().isNotFound());

        verify(cartService, times(1)).updateCart(eq(1L), any(Cart.class));
    }

    @DisplayName("Remove cart item test")
    @Test
    void removeCartItemTest() throws Exception {
        doNothing().when(cartService).removeProductFromCart(1L, 2L);

        mockMvc.perform(delete("/api/carts/1/products/2"))
                .andExpect(status().isOk());

        verify(cartService, times(1)).removeProductFromCart(1L, 2L);
    }

    @DisplayName("Add cart item to cart test")
    @Test
    void addCartItemToCartTest() throws Exception {
        doNothing().when(cartService).addProductToCart(1L, 2L, 3);

        mockMvc.perform(post("/api/carts/1/products/2")
                        .param("quantity", "3"))
                .andExpect(status().isOk());

        verify(cartService, times(1)).addProductToCart(1L, 2L, 3);
    }

    @DisplayName("Get cart total test")
    @Test
    void getCartTotalTest() throws Exception {
        when(cartService.sumTotal(1L)).thenReturn(100.0);

        mockMvc.perform(get("/api/carts/1/total"))
                .andExpect(status().isOk())
                .andExpect(content().string("100.0"));

        verify(cartService, times(1)).sumTotal(1L);
    }

    @DisplayName("Get cart item quantity test")
    @Test
    void getCartItemQuantityTest() throws Exception {
        when(cartService.currentQuantity(1L, 2L)).thenReturn(3);

        mockMvc.perform(get("/api/carts/1/products/2/quantity"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        verify(cartService, times(1)).currentQuantity(1L, 2L);
    }

    @DisplayName("Remove non-existent cart item test")
    @Test
    void removeNonExistentCartItemTest() throws Exception {
        doThrow(new CartItemNotFoundException(1L, 2L)).when(cartService).removeProductFromCart(1L, 2L);

        mockMvc.perform(delete("/api/carts/1/products/2"))
                .andExpect(status().isNotFound());

        verify(cartService, times(1)).removeProductFromCart(1L, 2L);
    }

    @DisplayName("Add cart item to non-existent cart test")
    @Test
    void addCartItemToNonExistentCartTest() throws Exception {
        doThrow(new CartNotFoundException(1L)).when(cartService).addProductToCart(1L, 2L, 3);

        mockMvc.perform(post("/api/carts/1/products/2")
                        .param("quantity", "3"))
                .andExpect(status().isNotFound());

        verify(cartService, times(1)).addProductToCart(1L, 2L, 3);
    }

    @DisplayName("Get total of non-existent cart test")
    @Test
    void getTotalOfNonExistentCartTest() throws Exception {
        when(cartService.sumTotal(1L)).thenThrow(new CartNotFoundException(1L));

        mockMvc.perform(get("/api/carts/1/total"))
                .andExpect(status().isNotFound());

        verify(cartService, times(1)).sumTotal(1L);
    }

    @DisplayName("Get quantity of non-existent cart item test")
    @Test
    void getQuantityOfNonExistentCartItemTest() throws Exception {
        when(cartService.currentQuantity(1L, 2L)).thenThrow(new CartItemNotFoundException(1L, 2L));

        mockMvc.perform(get("/api/carts/1/products/2/quantity"))
                .andExpect(status().isNotFound());

        verify(cartService, times(1)).currentQuantity(1L, 2L);
    }
}