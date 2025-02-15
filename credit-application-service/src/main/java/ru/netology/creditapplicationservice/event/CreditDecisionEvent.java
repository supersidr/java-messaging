package ru.netology.creditapplicationservice.event;

import java.io.Serializable;

public class CreditDecisionEvent implements Serializable {
    private Long id;
    private boolean approved;

    public CreditDecisionEvent() {}

    public CreditDecisionEvent(Long id, boolean approved) {
        this.id = id;
        this.approved = approved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "CreditDecisionEvent{" +
                "id=" + id +
                ", approved=" + approved +
                '}';
    }
}
