package com.Globant.E_commerce.jUnit.Controller;

import com.Globant.E_commerce.Customer.Customer;
import com.Globant.E_commerce.Customer.CustomerController;
import com.Globant.E_commerce.Customer.CustomerDTO;
import com.Globant.E_commerce.Customer.CustomerService;
import com.Globant.E_commerce.Util.ExceptionHandler.CustomerNotFoundException;
import com.Globant.E_commerce.Util.ExceptionHandler.GlobalExceptionHandler;
import com.Globant.E_commerce.Util.Mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;

    private Customer customerTest;
    private CustomerDTO customerDTOTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(customerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        customerTest = new Customer(1L, "John", "Doe", "john.doe@example.com", "123456789", LocalDate.of(1990, 1, 1));
        customerDTOTest = new CustomerDTO(1L, "John", "Doe", "john.doe@example.com", "123456789", LocalDate.of(1990, 1, 1));
    }

    @DisplayName("Create customer test")
    @Test
    void createCustomerTest() throws Exception {
        when(customerMapper.dtoToEntity(any(CustomerDTO.class))).thenReturn(customerTest);
        when(customerService.createCustomer(any(Customer.class))).thenReturn(customerTest);
        when(customerMapper.entityToDto(any(Customer.class))).thenReturn(customerDTOTest);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\", \"phone\": \"123456789\", \"birthDate\": \"1990-01-01\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("123456789"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));

        verify(customerService, times(1)).createCustomer(any(Customer.class));
    }

    @DisplayName("Get customer test")
    @Test
    void getCustomerByIdTest() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(customerTest);
        when(customerMapper.entityToDto(any(Customer.class))).thenReturn(customerDTOTest);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("123456789"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));

        verify(customerService, times(1)).getCustomerById(1L);
    }

    @DisplayName("Update customer test")
    @Test
    void updateCustomerTest() throws Exception {
        Customer updatedCustomer = new Customer(1L, "Jane", "Doe", "jane.doe@example.com", "987654321", LocalDate.of(1995, 5, 5));

        when(customerMapper.dtoToEntity(any(CustomerDTO.class))).thenReturn(updatedCustomer);
        when(customerService.updateCustomer(eq(1L), any(Customer.class))).thenReturn(updatedCustomer);
        when(customerMapper.entityToDto(any(Customer.class))).thenReturn(customerDTOTest);

        customerDTOTest.setName("Jane");
        customerDTOTest.setLastName("Doe");
        customerDTOTest.setEmail("jane.doe@example.com");
        customerDTOTest.setPhone("987654321");

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"name\": \"Jane\", \"lastName\": \"Doe\", \"email\": \"jane.doe@example.com\", \"phone\": \"987654321\", \"birthDate\": \"1995-05-05\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("jane.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("987654321"));

        verify(customerService, times(1)).updateCustomer(eq(1L), any(Customer.class));
    }

    @DisplayName("Delete customer test")
    @Test
    void deleteCustomerTest() throws Exception {
        when(customerService.deleteCustomer(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService, times(1)).deleteCustomer(1L);
    }

    @DisplayName("Try to get a non existent customer")
    @Test
    void getCustomerByIdNotFoundTest() throws Exception {
        when(customerService.getCustomerById(1L)).thenThrow(new CustomerNotFoundException(1L));

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isNotFound());

        verify(customerService, times(1)).getCustomerById(1L);
    }
}