import com.Globant.E_commerce.Cart.Cart;
import com.Globant.E_commerce.Cart.CartRepository;
import com.Globant.E_commerce.Cart.CartService;
import com.Globant.E_commerce.Cart.Status;
import com.Globant.E_commerce.CartItem.CartItem;
import com.Globant.E_commerce.Customer.Customer;
import com.Globant.E_commerce.Notification.EmailService;
import com.Globant.E_commerce.Notification.RabbitMQConfiguration;
import com.Globant.E_commerce.Product.Product;
import com.Globant.E_commerce.Product.ProductRepository;
import com.Globant.E_commerce.Product.Type;
import com.Globant.E_commerce.Util.ExceptionHandler.CartItemNotFoundException;
import com.Globant.E_commerce.Util.ExceptionHandler.CartNotFoundException;
import com.Globant.E_commerce.Util.ExceptionHandler.ProductNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private EmailService emailService;

    @Mock
    private Customer mockCustomer;

    @InjectMocks
    private CartService cartService;

    @Mock
    private Product mockProduct;

    private Cart cartTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockCustomer.getName()).thenReturn("Lionel Andrés");
        when(mockCustomer.getLastName()).thenReturn("Messi");
        when(mockCustomer.getEmail()).thenReturn("leomessi@gmail.com");
        when(mockCustomer.getPhone()).thenReturn("101010");
        when(mockCustomer.getId()).thenReturn(1L);
        when(mockCustomer.getBirthDate()).thenReturn(LocalDate.of(1987, 6, 24));

        when(mockProduct.getId()).thenReturn(1L);
        when(mockProduct.getName()).thenReturn("Laptop");
        when(mockProduct.getDescription()).thenReturn("Gaming laptop");
        when(mockProduct.getPrice()).thenReturn(1500.00);
        when(mockProduct.getType()).thenReturn(Type.ELECTRONIC);

        cartTest = new Cart(1L, mockCustomer, Status.DRAFT, new ArrayList<>());

        cartService = new CartService(cartRepository, productRepository, rabbitTemplate, emailService);
    }

    @DisplayName("Create new cart")
    @Test
    void createCartTest() {
        when(cartRepository.save(any(Cart.class))).thenReturn(cartTest);
        Cart createdCart = cartService.createCart(cartTest);
        assertNotNull(createdCart);
        assertEquals(cartTest.getId(), createdCart.getId());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @DisplayName("Update cart test - status change to SUBMITTED")
    @Test
    void updateCartStatusToSubmittedTest() {
        Cart existingCart = new Cart(1L, mockCustomer, Status.DRAFT, new ArrayList<>());
        Cart updatedCart = new Cart(1L, mockCustomer, Status.SUBMITTED, new ArrayList<>());

        when(cartRepository.findById(1L)).thenReturn(Optional.of(existingCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(updatedCart);
        
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        Cart result = cartService.updateCart(1L, updatedCart);

        assertEquals(Status.SUBMITTED, result.getStatus());
        verify(emailService, times(1)).sendEmail(
                eq("leomessi@gmail.com"),
                eq("Cart Submitted"),
                contains("Dear Lionel Andrés")
        );
        verify(rabbitTemplate, times(1)).convertAndSend(
                eq(RabbitMQConfiguration.EXCHANGE),
                eq(RabbitMQConfiguration.CART_ROUTING_KEY),
                anyString()
        );
    }

    @DisplayName("Try to update cart test without status change")
    @Test
    void updateCartNoStatusChangeTest() {
        Cart existingCart = new Cart(1L, mockCustomer, Status.DRAFT, new ArrayList<>());
        Cart updatedCart = new Cart(1L, mockCustomer, Status.DRAFT, new ArrayList<>());

        when(cartRepository.findById(1L)).thenReturn(Optional.of(existingCart));
        when(cartRepository.save(any(Cart.class))).thenReturn(updatedCart);

        Cart result = cartService.updateCart(1L, updatedCart);

        assertEquals(Status.DRAFT, result.getStatus());
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
        verify(rabbitTemplate, never()).convertAndSend(anyString(), anyString(), anyString());
    }

    @DisplayName("Try to update a non existent cart")
    @Test
    void updateNonExistentCartTest() {
        when(cartRepository.findById(cartTest.getId())).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> cartService.updateCart(cartTest.getId(), cartTest));
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @DisplayName("Get cart by id")
    @Test
    void getCartByIdTest() {
        when(cartRepository.findById(cartTest.getId())).thenReturn(Optional.of(cartTest));
        Cart foundCart = cartService.getCartById(cartTest.getId());
        assertNotNull(foundCart);
        assertEquals(cartTest.getId(), foundCart.getId());
        verify(cartRepository, times(1)).findById(cartTest.getId());
    }

    @DisplayName("Try to get cart by non existent id")
    @Test
    void getCartByNonExistentIdTest() {
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(CartNotFoundException.class, () -> cartService.getCartById(999L));
    }

    @DisplayName("Get all carts")
    @Test
    void getAllCartsTest() {
        List<Cart> carts = Arrays.asList(cartTest, new Cart());
        when(cartRepository.findAll()).thenReturn(carts);
        List<Cart> result = cartService.getAllCarts();
        assertEquals(2, result.size());
        verify(cartRepository, times(1)).findAll();
    }

    @DisplayName("Add product to cart with new item")
    @Test
    void addNewProductToCartTest() {
        when(cartRepository.findById(cartTest.getId())).thenReturn(Optional.of(cartTest));
        when(productRepository.findById(mockProduct.getId())).thenReturn(Optional.of(mockProduct));

        cartService.addProductToCart(cartTest.getId(), mockProduct.getId(), 2);
        assertEquals(1, cartTest.getCartItems().size());
        assertEquals(2, cartTest.getCartItems().get(0).getQuantity());
        verify(cartRepository, times(1)).save(cartTest);
    }

    @DisplayName("Add product to cart with existing item")
    @Test
    void addExistingProductToCartTest() {
        CartItem existingItem = new CartItem(1L, cartTest, mockProduct, 1);
        cartTest.getCartItems().add(existingItem);

        when(cartRepository.findById(cartTest.getId())).thenReturn(Optional.of(cartTest));
        when(productRepository.findById(mockProduct.getId())).thenReturn(Optional.of(mockProduct));

        cartService.addProductToCart(cartTest.getId(), mockProduct.getId(), 2);
        assertEquals(1, cartTest.getCartItems().size());
        assertEquals(3, cartTest.getCartItems().get(0).getQuantity());
        verify(cartRepository, times(1)).save(cartTest);
    }

    @DisplayName("Try to add a non existent product")
    @Test
    void addNonExistentProductTest() {
        when(cartRepository.findById(cartTest.getId())).thenReturn(Optional.of(cartTest));
        when(productRepository.findById(mockProduct.getId())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> cartService.addProductToCart(cartTest.getId(), mockProduct.getId(), 2));
    }

    @DisplayName("Remove product from cart (reduce quantity)")
    @Test
    void reduceCartItemQuantityTest() {
        CartItem cartItem = new CartItem(1L, cartTest, mockProduct, 3);
        cartTest.getCartItems().add(cartItem);

        when(cartRepository.findById(cartTest.getId())).thenReturn(Optional.of(cartTest));
        when(productRepository.findById(mockProduct.getId())).thenReturn(Optional.of(mockProduct));

        cartService.removeProductFromCart(cartTest.getId(), mockProduct.getId());

        assertEquals(2, cartItem.getQuantity());
        assertFalse(cartTest.getCartItems().isEmpty());
        verify(cartRepository, times(1)).save(cartTest);
    }

    @DisplayName("Remove product from cart")
    @Test
    void removeCartItemTest() {
        CartItem cartItem = new CartItem(1L, cartTest, mockProduct, 1);
        cartTest.getCartItems().add(cartItem);

        when(cartRepository.findById(cartTest.getId())).thenReturn(Optional.of(cartTest));
        when(productRepository.findById(mockProduct.getId())).thenReturn(Optional.of(mockProduct));

        cartService.removeProductFromCart(cartTest.getId(), mockProduct.getId());

        assertTrue(cartTest.getCartItems().isEmpty());
        verify(cartRepository, times(1)).save(cartTest);
    }

    @DisplayName("Try to remove a non existent product from cart")
    @Test
    void removeNonExistentProductTest() {
        when(cartRepository.findById(cartTest.getId())).thenReturn(Optional.of(cartTest));
        when(productRepository.findById(mockProduct.getId())).thenReturn(Optional.of(mockProduct));

        assertThrows(CartItemNotFoundException.class, () -> cartService.removeProductFromCart(cartTest.getId(), mockProduct.getId()));
        verify(cartRepository, never()).save(cartTest);
    }

    @DisplayName("Sum total of cart items")
    @Test
    void sumTotalTest() {
        CartItem cartItem1 = new CartItem(1L, cartTest, mockProduct, 2);
        Product mockProduct2 = mock(Product.class);
        when(mockProduct2.getPrice()).thenReturn(500.00);
        CartItem cartItem2 = new CartItem(2L, cartTest, mockProduct2, 1);
        cartTest.getCartItems().addAll(Arrays.asList(cartItem1, cartItem2));

        when(cartRepository.findById(cartTest.getId())).thenReturn(Optional.of(cartTest));

        double total = cartService.sumTotal(cartTest.getId());
        assertEquals(3500.0, total);
    }

    @DisplayName("Get current quantity of cart items")
    @Test
    void currentQuantityTest() {
        CartItem cartItem = new CartItem(1L, cartTest, mockProduct, 3);
        cartTest.getCartItems().add(cartItem);
        when(cartRepository.findById(cartTest.getId())).thenReturn(Optional.of(cartTest));
        when(productRepository.findById(mockProduct.getId())).thenReturn(Optional.of(mockProduct));

        int quantity = cartService.currentQuantity(cartTest.getId(), mockProduct.getId());
        assertEquals(3, quantity);
    }

    @DisplayName("Get current quantity of non existent cart item")
    @Test
    void currentQuantityNonExistentItemTest() {
        when(cartRepository.findById(cartTest.getId())).thenReturn(Optional.of(cartTest));
        when(productRepository.findById(mockProduct.getId())).thenReturn(Optional.of(mockProduct));

        int quantity = cartService.currentQuantity(cartTest.getId(), mockProduct.getId());
        assertEquals(0, quantity);
    }

    @DisplayName("Delete cart")
    @Test
    void deleteCartTest() {
        when(cartRepository.existsById(cartTest.getId())).thenReturn(true);
        doNothing().when(cartRepository).deleteById(cartTest.getId());

        boolean isDeleted = cartService.deleteCart(cartTest.getId());

        assertTrue(isDeleted);
        verify(cartRepository, times(1)).deleteById(cartTest.getId());
    }

    @DisplayName("Try to delete a non existent cart")
    @Test
    void deleteNonExistentCartTest() {
        when(cartRepository.existsById(cartTest.getId())).thenReturn(false);

        boolean isDeleted = cartService.deleteCart(cartTest.getId());

        assertFalse(isDeleted);
        verify(cartRepository, never()).deleteById(cartTest.getId());
    }
}