package ru.netology.creditprocessingservice.event;

import java.math.BigDecimal;

public class CreditApplicationEvent {
    private Long id;
    private BigDecimal amount;
    private Integer term;
    private BigDecimal income;
    private BigDecimal creditLoad;
    private Integer creditScore;

    public CreditApplicationEvent() {}

    public CreditApplicationEvent(Long id, BigDecimal amount, Integer term, BigDecimal income, BigDecimal creditLoad, Integer creditScore) {
        this.id = id;
        this.amount = amount;
        this.term = term;
        this.income = income;
        this.creditLoad = creditLoad;
        this.creditScore = creditScore;
    }

    public Long getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public Integer getTerm() { return term; }
    public BigDecimal getIncome() { return income; }
    public BigDecimal getCreditLoad() { return creditLoad; }
    public Integer getCreditScore() { return creditScore; }
}

