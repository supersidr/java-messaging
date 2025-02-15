package ru.netology.creditapplicationservice.event;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "credit_applications")
public class CreditApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;
    private Integer term;
    private BigDecimal income;
    private BigDecimal creditLoad;
    private Integer creditScore;

    @Enumerated(EnumType.STRING)
    private CreditStatus status = CreditStatus.IN_PROGRESS;

    // Геттеры и сеттеры
}