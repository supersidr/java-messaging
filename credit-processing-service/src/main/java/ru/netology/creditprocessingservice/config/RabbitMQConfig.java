package ru.netology.creditprocessingservice.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange creditDecisionExchange() {
        return new DirectExchange("credit-decision-exchange");
    }

    @Bean
    public Queue creditDecisionQueue() {
        return new Queue("credit-decision-queue", false);
    }

    @Bean
    public Binding binding(Queue creditDecisionQueue, DirectExchange creditDecisionExchange) {
        return BindingBuilder.bind(creditDecisionQueue).to(creditDecisionExchange).with("credit.decision");
    }
}

