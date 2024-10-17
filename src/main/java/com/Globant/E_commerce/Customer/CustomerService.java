package com.Globant.E_commerce.Customer;

import com.Globant.E_commerce.Notification.EmailService;
import com.Globant.E_commerce.Notification.RabbitMQConfiguration;
import com.Globant.E_commerce.Util.ExceptionHandler.CustomerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private final CustomerRepository customerRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        return customerRepository.findById(id).map(existingCustomer -> {
            existingCustomer.setName(updatedCustomer.getName() != null ? updatedCustomer.getName() : existingCustomer.getName());
            existingCustomer.setLastName(updatedCustomer.getLastName() != null ? updatedCustomer.getLastName() : existingCustomer.getLastName());
            existingCustomer.setEmail(updatedCustomer.getEmail() != null ? updatedCustomer.getEmail() : existingCustomer.getEmail());
            existingCustomer.setPhone(updatedCustomer.getPhone() != null ? updatedCustomer.getPhone() : existingCustomer.getPhone());
            existingCustomer.setBirthDate(updatedCustomer.getBirthDate() != null ? updatedCustomer.getBirthDate() : existingCustomer.getBirthDate());
            return customerRepository.save(existingCustomer);
        }).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Override
    public boolean deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            logger.warn("Customer not found with id: " + id);
            return false;
        }
        customerRepository.deleteById(id);
        return true;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void checkBirthdays() {
        List<Customer> customers = customerRepository.findAll();
        LocalDate today = LocalDate.now();

        for (Customer customer : customers) {
            if (customer.getBirthDate().equals(today)) {
                String email = customer.getEmail();
                String message = "Happy Birthday, " + customer.getName() + "!";

                emailService.sendEmail(email, "Happy Birthday!", message);

                rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE, "birthday", message);
            }
        }
    }
}