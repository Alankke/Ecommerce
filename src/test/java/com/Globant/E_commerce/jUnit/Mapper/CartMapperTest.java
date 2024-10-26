package com.Globant.E_commerce.jUnit.Mapper;

import com.Globant.E_commerce.Cart.Cart;
import com.Globant.E_commerce.Cart.CartDTO;
import com.Globant.E_commerce.Cart.Status;
import com.Globant.E_commerce.CartItem.CartItem;
import com.Globant.E_commerce.CartItem.CartItemDTO;
import com.Globant.E_commerce.Customer.Customer;
import com.Globant.E_commerce.Customer.CustomerRepository;
import com.Globant.E_commerce.Product.Product;
import com.Globant.E_commerce.Product.Type;
import com.Globant.E_commerce.Util.ExceptionHandler.CustomerNotFoundException;
import com.Globant.E_commerce.Util.Mapper.CartMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartMapperTest {

    private CartMapper cartMapper;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartMapper = new CartMapper();
    }

    @Test
    void entityToDtoWithItemsTest() {
        Customer customer = new Customer();
        customer.setId(1L);

        Product product1 = new Product(1L, "Product 1", "Description 1", 10.0, Type.ELECTRONIC);
        Product product2 = new Product(2L, "Product 2", "Description 2", 20.0, Type.LIBRARY);

        CartItem item1 = new CartItem();
        item1.setProduct(product1);
        item1.setQuantity(2);

        CartItem item2 = new CartItem();
        item2.setProduct(product2);
        item2.setQuantity(1);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setCustomer(customer);
        cart.setStatus(Status.DRAFT);
        cart.setCartItems(Arrays.asList(item1, item2));

        CartDTO result = cartMapper.entityToDto(cart);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getCustomerId());
        assertEquals("DRAFT", result.getStatus());
        assertEquals(2, result.getCartItems().size());

        CartItemDTO resultItem1 = result.getCartItems().get(0);
        assertEquals(1L, resultItem1.getProductId());
        assertEquals("Product 1", resultItem1.getProductName());
        assertEquals(10.0, resultItem1.getProductPrice());
        assertEquals(2, resultItem1.getQuantity());

        CartItemDTO resultItem2 = result.getCartItems().get(1);
        assertEquals(2L, resultItem2.getProductId());
        assertEquals("Product 2", resultItem2.getProductName());
        assertEquals(20.0, resultItem2.getProductPrice());
        assertEquals(1, resultItem2.getQuantity());
    }

    @Test
    void entityToDtoWithoutItemsTest() {
        Customer customer = new Customer();
        customer.setId(1L);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setCustomer(customer);
        cart.setStatus(Status.DRAFT);
        cart.setCartItems(null);

        CartDTO result = cartMapper.entityToDto(cart);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getCustomerId());
        assertEquals("DRAFT", result.getStatus());
        assertTrue(result.getCartItems().isEmpty());
    }

    @Test
    void dtoToEntityTest() {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(1L);
        cartDTO.setCustomerId(1L);
        cartDTO.setStatus("SUBMITTED");

        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Cart result = cartMapper.dtoToEntity(cartDTO, customerRepository);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(customer, result.getCustomer());
        assertEquals(Status.SUBMITTED, result.getStatus());

        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void dtoToEntityCustomerNotFoundTest() {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(1L);
        cartDTO.setCustomerId(1L);
        cartDTO.setStatus("DRAFT");

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> cartMapper.dtoToEntity(cartDTO, customerRepository));
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void dtoToEntityInvalidStatusTest() {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(1L);
        cartDTO.setCustomerId(1L);
        cartDTO.setStatus("INVALID_STATUS");

        Customer customer = new Customer();
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThrows(IllegalArgumentException.class, () -> cartMapper.dtoToEntity(cartDTO, customerRepository));
        verify(customerRepository, times(1)).findById(1L);
    }
}
