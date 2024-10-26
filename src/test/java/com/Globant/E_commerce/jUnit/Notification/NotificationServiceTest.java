package com.Globant.E_commerce.jUnit.Notification;

import com.Globant.E_commerce.Cart.Cart;
import com.Globant.E_commerce.Cart.CartRepository;
import com.Globant.E_commerce.Customer.Customer;
import com.Globant.E_commerce.Customer.CustomerRepository;
import com.Globant.E_commerce.Notification.EmailService;
import com.Globant.E_commerce.Notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendBirthdayNotification() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("John");
        customer.setEmail("john@example.com");
        customer.setBirthDate(LocalDate.now().minusYears(30));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        notificationService.sendBirthdayNotification(customerId.toString());

        verify(emailService, times(1)).sendEmail(
                eq("john@example.com"),
                eq("Happy Birthday, John!"),
                contains("Wishing you a very happy birthday")
        );
    }

    @Test
    void testSendCartSubmittedNotification() {
        Long cartId = 1L;
        Cart cart = new Cart();
        cart.setId(cartId);

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Alice");
        customer.setEmail("alice@example.com");

        cart.setCustomer(customer);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        notificationService.sendCartSubmittedNotification(cartId.toString());

        verify(emailService, times(1)).sendEmail(
                eq("alice@example.com"),
                eq("Your Cart has been Submitted!"),
                contains("Your cart with ID 1 has been successfully submitted")
        );
    }

    @Test
    void testGetNotification() {
        String message = "Test notification";
        notificationService.getNotification(message);
        // This test is mainly to ensure the method doesn't throw any exceptions
        // You might want to use a mocked logger to verify the log message in a real scenario
    }
}