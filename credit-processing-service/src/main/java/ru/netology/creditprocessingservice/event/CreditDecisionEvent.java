package ru.netology.creditprocessingservice.event;

public class CreditDecisionEvent {
    private Long id;
    private boolean approved;

    public CreditDecisionEvent() {}

    public CreditDecisionEvent(Long id, boolean approved) {
        this.id = id;
        this.approved = approved;
    }

    public Long getId() { return id; }
    public boolean isApproved() { return approved; }
}
