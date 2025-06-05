package com.visitas.visitas.visitas.domain.usecases;

import com.visitas.visitas.visitas.domain.exceptions.InvalidException;
import com.visitas.visitas.visitas.domain.model.ScheduledVisitModel;
import com.visitas.visitas.visitas.domain.model.VisitsModel;
import com.visitas.visitas.visitas.domain.ports.in.ScheduledVisitServicePort;
import com.visitas.visitas.visitas.domain.ports.out.ScheduledVisitPersistencePort;
import com.visitas.visitas.visitas.domain.ports.out.VisitsPersistencePort;
import com.visitas.visitas.visitas.domain.utils.constants.DomainConstants;

import java.time.LocalDateTime;
import java.util.Optional;

public class ScheduledVisitUseCase implements ScheduledVisitServicePort {

    private final ScheduledVisitPersistencePort scheduledVisitPersistencePort;
    private final VisitsPersistencePort visitsPersistencePort;

    public ScheduledVisitUseCase(
            ScheduledVisitPersistencePort scheduledVisitPersistencePort,
            VisitsPersistencePort visitsPersistencePort
    ) {
        this.scheduledVisitPersistencePort = scheduledVisitPersistencePort;
        this.visitsPersistencePort = visitsPersistencePort;
    }

    @Override
    public ScheduledVisitModel save(ScheduledVisitModel scheduledVisit) {
        Optional<VisitsModel> visitSlotOpt = visitsPersistencePort.findById(scheduledVisit.getVisitId());
        if (visitSlotOpt.isEmpty()) {
            throw new InvalidException(DomainConstants.VISIT_NOT_FOUND);
        }
        VisitsModel visitSlot = visitSlotOpt.get();


        LocalDateTime now = LocalDateTime.now();
        if (visitSlot.getStartDateTime().isBefore(now)) {
            throw new InvalidException(DomainConstants.VISIT_START_IN_PAST);
        }

        int existingCount = scheduledVisitPersistencePort.countByVisitId(scheduledVisit.getVisitId());
        if (existingCount >= DomainConstants.MAX_SCHEDULED_PER_SLOT) {
            throw new InvalidException(DomainConstants.VISIT_SLOT_FULL);
        }

        boolean alreadyScheduled = scheduledVisitPersistencePort.existsByVisitIdAndBuyerId(
                scheduledVisit.getVisitId(),
                scheduledVisit.getBuyerId()
        );

        int totalScheduled = scheduledVisitPersistencePort.countByVisitId(scheduledVisit.getVisitId());
        if (totalScheduled >= 2) {
            throw new InvalidException(DomainConstants.VISIT_FULL);
        }

        if (alreadyScheduled) {
            throw new InvalidException(DomainConstants.VISIT_ALREADY_SCHEDULED_BY_USER);
        }

        return scheduledVisitPersistencePort.save(scheduledVisit);
    }
}

