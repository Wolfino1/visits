package com.visitas.visitas.visitas.domain.ports.out;

import com.visitas.visitas.visitas.domain.model.VisitsModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface VisitsPersistencePort {
    boolean existsOverlap(Long sellerId, LocalDateTime start, LocalDateTime end);
    VisitsModel save(VisitsModel visit);
    int getScheduledPersonCount(Long visitId);
    Long getLocationId(Long visitId);
    Page<VisitsModel> findWithFilters(
            LocalDateTime startFrom,
            LocalDateTime startTo,
            LocalDateTime endFrom,
            LocalDateTime endTo,
            Long locationId,
            LocalDateTime now,
            int maxScheduled,
            Pageable pageable
    );

}
