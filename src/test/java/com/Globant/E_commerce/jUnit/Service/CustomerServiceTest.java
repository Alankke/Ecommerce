package com.Globant.E_commerce.jUnit.Service;


import com.Globant.E_commerce.Customer.Customer;
import com.Globant.E_commerce.Customer.CustomerRepository;
import com.Globant.E_commerce.Customer.CustomerService;
import com.Globant.E_commerce.Notification.EmailService;
import com.Globant.E_commerce.Notification.RabbitMQConfiguration;
import com.Globant.E_commerce.Util.ExceptionHandler.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CustomerService customerService;

    private Customer customerTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        customerTest = new Customer(1L, "Lionel Andrés", "Messi", "leomessi@gmail.com", "101010", LocalDate.of(1987, 6, 24));
        when(customerRepository.save(any(Customer.class))).thenReturn(customerTest);
    }

    @DisplayName("Create customer test")
    @Test
    void createCustomerTest() {
        Customer createdCustomer = customerService.createCustomer(customerTest);

        assertNotNull(createdCustomer);
        assertEquals(customerTest.getId(), createdCustomer.getId());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @DisplayName("Get customer by id test")
    @Test
    void getCustomerByIdTest() {
        when(customerRepository.findById(customerTest.getId())).thenReturn(Optional.of(customerTest));
        
        Customer foundCustomer = customerService.getCustomerById(customerTest.getId());
        
        assertNotNull(foundCustomer);
        assertEquals(customerTest.getId(), foundCustomer.getId());
        verify(customerRepository, times(1)).findById(customerTest.getId());
    }

    @Test
    @DisplayName("Try to get non existent customer by id")
    void getCustomerByIdNotFoundTest() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomerById(1L));
        verify(customerRepository, times(1)).findById(1L);
    }

    @DisplayName("Get all customers test")
    @Test
    void getAllCustomersTest() {
        List<Customer> customers = List.of(customerTest);
        when(customerRepository.findAll()).thenReturn(customers);
        
        List<Customer> foundCustomers = customerService.getAllCustomers();
        
        assertNotNull(foundCustomers);
        assertEquals(1, foundCustomers.size());
        verify(customerRepository, times(1)).findAll();
    }

    @DisplayName("Update customer with new fields test")
    @Test
    void updateCustomerTest() {
        Customer updatedCustomer = new Customer(customerTest.getId(), "UpdatedName", "UpdatedLastName",
                "updated.email@example.com", "123456789", LocalDate.of(1990, 1, 1));

        when(customerRepository.findById(customerTest.getId())).thenReturn(Optional.of(customerTest));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        Customer result = customerService.updateCustomer(customerTest.getId(), updatedCustomer);

        assertNotNull(result);
        
        assertEquals("UpdatedName", result.getName());
        assertEquals("UpdatedLastName", result.getLastName());
        assertEquals("updated.email@example.com", result.getEmail());
        assertEquals("123456789", result.getPhone());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthDate());

        verify(customerRepository, times(1)).save(updatedCustomer);
    }

    @DisplayName("Update a customer with null fields test")
    @Test
    void updateCustomerNullFieldsTest() {
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(customerTest.getId()); 

        when(customerRepository.findById(customerTest.getId())).thenReturn(Optional.of(customerTest));
        when(customerRepository.save(any(Customer.class))).thenReturn(customerTest);

        Customer result = customerService.updateCustomer(customerTest.getId(), updatedCustomer);

        assertNotNull(result);
        
        assertEquals(customerTest.getName(), result.getName());
        assertEquals(customerTest.getLastName(), result.getLastName());
        assertEquals(customerTest.getEmail(), result.getEmail());
        assertEquals(customerTest.getPhone(), result.getPhone());
        assertEquals(customerTest.getBirthDate(), result.getBirthDate());

        verify(customerRepository, times(1)).save(customerTest);
    }

    @Test
    @DisplayName("Try to update non existent customer")
    void updateCustomerNotFoundTest() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(1L, new Customer()));
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @DisplayName("Delete customer test")
    @Test
    void deleteCustomerTest() {
        when(customerRepository.existsById(customerTest.getId())).thenReturn(true);
        doNothing().when(customerRepository).deleteById(customerTest.getId());

        boolean isDeleted = customerService.deleteCustomer(customerTest.getId());

        assertTrue(isDeleted);
        verify(customerRepository, times(1)).deleteById(customerTest.getId());
    }

    @DisplayName("Try to delete a non existent customer")
    @Test
    void deleteNonExistentCustomerTest() {
        when(customerRepository.existsById(customerTest.getId())).thenReturn(false);

        boolean isDeleted = customerService.deleteCustomer(customerTest.getId());

        assertFalse(isDeleted);
        verify(customerRepository, times(0)).deleteById(customerTest.getId());
    }
}