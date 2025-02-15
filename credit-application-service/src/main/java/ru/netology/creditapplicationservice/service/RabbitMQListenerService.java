package ru.netology.creditapplicationservice.service;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.netology.creditapplicationservice.event.CreditApplication;
import ru.netology.creditapplicationservice.event.CreditStatus;
import ru.netology.creditapplicationservice.repository.CreditApplicationRepository;

import java.util.Optional;

@Service
public class RabbitMQListenerService {

    private final CreditApplicationRepository repository;

    public RabbitMQListenerService(CreditApplicationRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "credit-decision-queue")
    public void receiveDecision(CreditDecisionEvent event) {
        Optional<CreditApplication> applicationOpt = repository.findById(event.getId());
        applicationOpt.ifPresent(application -> {
            application.setStatus(event.isApproved() ? CreditStatus.APPROVED : CreditStatus.REJECTED);
            repository.save(application);
            System.out.println("Updated application ID " + event.getId() + " with status: " + application.getStatus());
        });
    }
}
