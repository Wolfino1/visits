package com.visitas.visitas.visitas.domain.ports.out;

import com.visitas.visitas.visitas.domain.model.ScheduledVisitModel;

public interface ScheduledVisitPersistencePort {
    ScheduledVisitModel save(ScheduledVisitModel scheduledVisit);
    int countByVisitId(Long visitId);
    boolean existsByVisitIdAndBuyerId(Long visitId, Long buyerId);
}
