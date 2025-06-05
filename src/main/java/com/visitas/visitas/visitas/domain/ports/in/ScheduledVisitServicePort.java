package com.visitas.visitas.visitas.domain.ports.in;

import com.visitas.visitas.visitas.domain.model.ScheduledVisitModel;

public interface ScheduledVisitServicePort {
    ScheduledVisitModel save(ScheduledVisitModel scheduledVisit);

}
