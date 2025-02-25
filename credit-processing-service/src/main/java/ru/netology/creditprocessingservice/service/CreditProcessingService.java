package ru.netology.creditprocessingservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.netology.creditapplicationservice.event.CreditApplicationEvent;
import ru.netology.creditapplicationservice.event.CreditDecisionEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CreditProcessingService {
    private final RabbitTemplate rabbitTemplate;

    @KafkaListener(topics = "credit-applications", groupId = "credit-processing-service")
    public void processApplication(CreditApplicationEvent event) {
        BigDecimal monthlyPayment = calculateMonthlyPayment(event.getAmount(), event.getTerm());
        BigDecimal totalMonthlyPayments = monthlyPayment.add(event.getCurrentCreditLoad());
        boolean approved = totalMonthlyPayments.compareTo(event.getIncome().multiply(BigDecimal.valueOf(0.5))) <= 0;

        CreditDecisionEvent decision = new CreditDecisionEvent(event.getApplicationId(), approved);
        rabbitTemplate.convertAndSend("credit-decisions", decision);
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal amount, Integer term) {
        return amount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);
    }
}

