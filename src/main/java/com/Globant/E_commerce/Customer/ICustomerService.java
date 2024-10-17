package com.Globant.E_commerce.Customer;

import java.util.List;

public interface ICustomerService {
    Customer createCustomer(Customer customer);
    Customer getCustomerById(Long id);
    List<Customer> getAllCustomers();
    Customer updateCustomer(Long id, Customer customer);
    boolean deleteCustomer(Long id);
}
