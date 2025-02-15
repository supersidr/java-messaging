package ru.netology.creditapplicationservice.controller;



import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.netology.creditapplicationservice.event.CreditApplication;
import ru.netology.creditapplicationservice.event.CreditApplicationEvent;
import ru.netology.creditapplicationservice.event.CreditStatus;
import ru.netology.creditapplicationservice.repository.CreditApplicationRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/credits")
public class CreditApplicationController {

    private final CreditApplicationRepository repository;
    private final KafkaTemplate<String, CreditApplicationEvent> kafkaTemplate;

    public CreditApplicationController(CreditApplicationRepository repository, KafkaTemplate<String, CreditApplicationEvent> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public Long createCreditApplication(@RequestBody CreditApplication application) {
        application.setStatus(CreditStatus.IN_PROGRESS);
        repository.save(application);

        // Отправляем заявку в Kafka
        CreditApplicationEvent event = new CreditApplicationEvent(application.getId(),
                application.getAmount(), application.getTerm(), application.getIncome(),
                application.getCreditLoad(), application.getCreditScore());

        kafkaTemplate.send("credit-application-topic", event);

        return application.getId();
    }

    @GetMapping("/{id}")
    public Optional<CreditApplication> getCreditStatus(@PathVariable Long id) {
        return repository.findById(id);
    }
}
