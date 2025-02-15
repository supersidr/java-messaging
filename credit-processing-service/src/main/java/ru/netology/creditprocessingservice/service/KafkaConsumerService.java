package ru.netology.creditprocessingservice.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.netology.creditprocessingservice.event.CreditApplicationEvent;
import ru.netology.creditprocessingservice.event.CreditDecisionEvent;

import java.math.BigDecimal;

@Service
public class KafkaConsumerService {

    private final RabbitTemplate rabbitTemplate;

    public KafkaConsumerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @KafkaListener(topics = "credit-application-topic", groupId = "credit-group")
    public void processCreditApplication(CreditApplicationEvent event) {
        boolean approved = isCreditApproved(event);

        CreditDecisionEvent decisionEvent = new CreditDecisionEvent(event.getId(), approved);
        rabbitTemplate.convertAndSend("credit-decision-exchange", "credit.decision", decisionEvent);

        System.out.println("Processed credit application ID: " + event.getId() + ", Approved: " + approved);
    }

    private boolean isCreditApproved(CreditApplicationEvent event) {
        BigDecimal maxPayment = event.getIncome().multiply(new BigDecimal("0.5"));
        BigDecimal monthlyPayment = event.getAmount().divide(new BigDecimal(event.getTerm()), BigDecimal.ROUND_HALF_UP);
        return monthlyPayment.compareTo(maxPayment) <= 0;
    }
}
