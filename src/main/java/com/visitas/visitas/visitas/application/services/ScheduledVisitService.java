package com.visitas.visitas.visitas.application.services;

import com.visitas.visitas.visitas.application.dto.request.SaveScheduledVisitRequest;
import com.visitas.visitas.visitas.application.dto.response.ScheduledVisitResponse;

public interface ScheduledVisitService {
    ScheduledVisitResponse save(SaveScheduledVisitRequest request, Long sellerId);
}
