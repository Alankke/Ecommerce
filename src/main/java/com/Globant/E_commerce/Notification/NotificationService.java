package com.Globant.E_commerce.Notification;

import com.Globant.E_commerce.Cart.Cart;
import com.Globant.E_commerce.Cart.CartRepository;
import com.Globant.E_commerce.Customer.Customer;
import com.Globant.E_commerce.Customer.CustomerRepository;
import com.Globant.E_commerce.Util.ExceptionHandler.CartNotFoundException;
import com.Globant.E_commerce.Util.ExceptionHandler.CustomerNotFoundException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final EmailService emailService;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;

    @Autowired
    public NotificationService(EmailService emailService, CustomerRepository customerRepository, CartRepository cartRepository) {
        this.emailService = emailService;
        this.customerRepository = customerRepository;
        this.cartRepository = cartRepository;
    }


    @RabbitListener(queues = RabbitMQConfiguration.BIRTHDAY_QUEUE)
    public void sendBirthdayNotification(String message) {
        Long customerId = Long.parseLong(message);

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        String subject = "Happy Birthday, " + customer.getName() + "!";
        String body = "Dear " + customer.getName() + ",\n\nWishing you a very happy birthday from our team!\n\nBest regards,\nE-commerce Alan";

        emailService.sendEmail(customer.getEmail(), subject, body);

        System.out.println("Sent birthday email to: " + customer.getEmail());
    }


    @RabbitListener(queues = RabbitMQConfiguration.CART_SUBMITTED_QUEUE)
    public void sendCartSubmittedNotification(String message) {
        Long cartId = Long.parseLong(message);

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException(cartId));

        Customer customer = cart.getCustomer();

        String subject = "Your Cart has been Submitted!";
        String body = "Dear " + customer.getName() + ",\n\nYour cart with ID " + cartId + " has been successfully submitted.\n\nBest regards,\nYour Company";

        emailService.sendEmail(customer.getEmail(), subject, body);

        System.out.println("Sent cart submission email to: " + customer.getEmail());
    }

    @RabbitListener(queues = RabbitMQConfiguration.CART_SUBMITTED_QUEUE)
    public void getNotification(String message) {
        System.out.println("Received notification: " + message);
    }
}