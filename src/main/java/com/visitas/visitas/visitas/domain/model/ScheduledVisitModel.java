package com.visitas.visitas.visitas.domain.model;

public class ScheduledVisitModel {
    private Long id;
    private Long visitId;
    private Long buyerId;

    public ScheduledVisitModel() {
    }

    public ScheduledVisitModel(Long id, Long visitId, Long buyerId) {
        this.id = id;
        this.visitId = visitId;
        this.buyerId = buyerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVisitId() {
        return visitId;
    }

    public void setVisitId(Long visitId) {
        this.visitId = visitId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }
}
