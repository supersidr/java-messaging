package ru.netology.creditapplicationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.netology.creditapplicationservice.dto.CreditApplicationListResponse;
import ru.netology.creditapplicationservice.dto.CreditApplicationRequest;
import ru.netology.creditapplicationservice.entity.CreditApplication;
import ru.netology.creditapplicationservice.event.CreditApplicationEvent;
import ru.netology.creditapplicationservice.event.CreditDecisionEvent;
import ru.netology.creditapplicationservice.model.ApplicationStatus;
import ru.netology.creditapplicationservice.repository.CreditApplicationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditApplicationService {
    private final CreditApplicationRepository repository;
    private final KafkaTemplate<String, CreditApplicationEvent> kafkaTemplate;

    public Long createApplication(CreditApplicationRequest request) {
        CreditApplication application = new CreditApplication();
        BeanUtils.copyProperties(request, application);
        application = repository.save(application);

        CreditApplicationEvent event = new CreditApplicationEvent(
                application.getId(),
                application.getAmount(),
                application.getTerm(),
                application.getIncome(),
                application.getCurrentCreditLoad(),
                application.getCreditRating()
        );

        kafkaTemplate.send("credit-applications", event);
        return application.getId();
    }

    public ApplicationStatus getApplicationStatus(Long id) {
        return repository.findById(id)
                .map(CreditApplication::getStatus)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @RabbitListener(queues = "credit-decisions")
    public void handleCreditDecision(CreditDecisionEvent event) {
        repository.findById(event.getApplicationId())
                .ifPresent(application -> {
                    application.setStatus(event.isApproved() ?
                            ApplicationStatus.APPROVED : ApplicationStatus.REJECTED);
                    repository.save(application);
                });
    }

    public List<CreditApplicationListResponse> getAllApplications() {
        return repository.findAll().stream()
                .map(app -> new CreditApplicationListResponse(
                        app.getId(),
                        app.getAmount(),
                        app.getTerm(),
                        app.getIncome(),
                        app.getCurrentCreditLoad(),
                        app.getCreditRating(),
                        app.getStatus()
                ))
                .collect(Collectors.toList());
    }
}
