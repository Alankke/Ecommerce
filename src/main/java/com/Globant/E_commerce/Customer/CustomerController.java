package com.Globant.E_commerce.Customer;

import com.Globant.E_commerce.Util.Mapper.CustomerMapper;
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
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @Operation(summary = "Create a new customer", description = "Create a new customer with the provided details.")
    @ApiResponse(responseCode = "201", description = "Customer created successfully")
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(
            @Valid @RequestBody CustomerDTO customerDTO) {
        Customer customer = customerService.createCustomer(customerMapper.dtoToEntity(customerDTO));
        return new ResponseEntity<>(customerMapper.entityToDto(customer), HttpStatus.CREATED);
    }

    @Operation(summary = "Get customer by ID", description = "Retrieve a customer's details by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(
            @Parameter(description = "ID of the customer to retrieve", required = true)
            @PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        return new ResponseEntity<>(customerMapper.entityToDto(customer), HttpStatus.OK);
    }

    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers.")
    @ApiResponse(responseCode = "200", description = "Customers retrieved successfully")
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerDTO> customerDTOs = customers.stream().map(customerMapper::entityToDto).collect(Collectors.toList());
        return new ResponseEntity<>(customerDTOs, HttpStatus.OK);
    }

    @Operation(summary = "Update customer", description = "Update the details of an existing customer by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @Parameter(description = "ID of the customer to update", required = true)
            @PathVariable Long id,
            @Valid @RequestBody CustomerDTO customerDTO) {
        Customer updatedCustomer = customerService.updateCustomer(id, customerMapper.dtoToEntity(customerDTO));
        return new ResponseEntity<>(customerMapper.entityToDto(updatedCustomer), HttpStatus.OK);
    }

    @Operation(summary = "Delete customer", description = "Delete a customer by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @Parameter(description = "ID of the customer to delete", required = true)
            @PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}