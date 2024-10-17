package com.Globant.E_commerce.jUnit.Mapper;

import com.Globant.E_commerce.Customer.Customer;
import com.Globant.E_commerce.Customer.CustomerDTO;
import com.Globant.E_commerce.Util.Mapper.CustomerMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CustomerMapperTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    void setUp() {
        customerMapper = new CustomerMapper();
    }

    @Test
    void entityToDtoTest() {
        Customer customer = Customer.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("1234567890")
                .birthDate(LocalDate.of(1990, 1, 1))
                .build();

        CustomerDTO result = customerMapper.entityToDto(customer);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("1234567890", result.getPhone());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthDate());
    }

    @Test
    void entityToDtoNullValuesTest() {
        Customer customer = Customer.builder()
                .id(1L)
                .build();

        CustomerDTO result = customerMapper.entityToDto(customer);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNull(result.getName());
        assertNull(result.getLastName());
        assertNull(result.getEmail());
        assertNull(result.getPhone());
        assertNull(result.getBirthDate());
    }

    @Test
    void dtoToEntityTest() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(1L)
                .name("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .phone("0987654321")
                .birthDate(LocalDate.of(1995, 5, 5))
                .build();

        Customer result = customerMapper.dtoToEntity(customerDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Jane", result.getName());
        assertEquals("Smith", result.getLastName());
        assertEquals("jane.smith@example.com", result.getEmail());
        assertEquals("0987654321", result.getPhone());
        assertEquals(LocalDate.of(1995, 5, 5), result.getBirthDate());
    }

    @Test
    void dtoToEntityNullValuesTest() {
        CustomerDTO customerDTO = CustomerDTO.builder()
                .id(1L)
                .build();

        Customer result = customerMapper.dtoToEntity(customerDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNull(result.getName());
        assertNull(result.getLastName());
        assertNull(result.getEmail());
        assertNull(result.getPhone());
        assertNull(result.getBirthDate());
    }

    @Test
    void roundTripMappingTest() {
        Customer originalCustomer = Customer.builder()
                .id(1L)
                .name("Alice")
                .lastName("Johnson")
                .email("alice.johnson@example.com")
                .phone("1122334455")
                .birthDate(LocalDate.of(1985, 3, 15))
                .build();

        CustomerDTO dto = customerMapper.entityToDto(originalCustomer);
        Customer resultCustomer = customerMapper.dtoToEntity(dto);

        assertNotNull(resultCustomer);
        assertEquals(originalCustomer.getId(), resultCustomer.getId());
        assertEquals(originalCustomer.getName(), resultCustomer.getName());
        assertEquals(originalCustomer.getLastName(), resultCustomer.getLastName());
        assertEquals(originalCustomer.getEmail(), resultCustomer.getEmail());
        assertEquals(originalCustomer.getPhone(), resultCustomer.getPhone());
        assertEquals(originalCustomer.getBirthDate(), resultCustomer.getBirthDate());
    }
}
