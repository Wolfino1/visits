package com.visitas.visitas.visitas.domain.ports.in;

import com.visitas.visitas.visitas.domain.model.VisitsModel;
import com.visitas.visitas.visitas.domain.utils.page.PagedResult;

import java.time.LocalDateTime;

public interface VisitsServicePort {
    VisitsModel save(VisitsModel visit);
    PagedResult<VisitsModel> getFilters(
            Integer page,
            Integer size,
            LocalDateTime startFrom,
            LocalDateTime startTo,
            LocalDateTime endFrom,
            LocalDateTime endTo,
            Long locationId,
            String sortBy,
            boolean orderAsc
    );
}
