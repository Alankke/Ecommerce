package com.Globant.E_commerce.Util.Mapper;

import com.Globant.E_commerce.Customer.Customer;
import com.Globant.E_commerce.Customer.CustomerDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDTO entityToDto(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .birthDate(customer.getBirthDate())
                .build();
    }

    public Customer dtoToEntity(CustomerDTO customerDTO) {
        return Customer.builder()
                .id(customerDTO.getId())
                .name(customerDTO.getName())
                .lastName(customerDTO.getLastName())
                .email(customerDTO.getEmail())
                .phone(customerDTO.getPhone())
                .birthDate(customerDTO.getBirthDate())
                .build();
    }
}