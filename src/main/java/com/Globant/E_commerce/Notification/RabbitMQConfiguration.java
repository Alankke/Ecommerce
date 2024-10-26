package com.Globant.E_commerce.Notification;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    public static final String BIRTHDAY_QUEUE = "birthdayNotificationQueue";
    public static final String CART_SUBMITTED_QUEUE = "cartSubmittedNotificationQueue";
    public static final String EXCHANGE = "notificationExchange";
    public static final String CART_ROUTING_KEY = "cartSubmitted";


    @Bean
    public Queue birthdayNotificationQueue() {
        return new Queue(BIRTHDAY_QUEUE, false);
    }

    @Bean
    public Queue cartSubmittedNotificationQueue() {
        return new Queue(CART_SUBMITTED_QUEUE, false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding birthdayBinding(Queue birthdayNotificationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(birthdayNotificationQueue).to(exchange).with("birthday");
    }

    @Bean
    public Binding cartSubmittedBinding(Queue cartSubmittedNotificationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(cartSubmittedNotificationQueue).to(exchange).with("cartSubmitted");
    }
}