package com.visitas.visitas.visitas.application.services;

import com.visitas.visitas.visitas.application.dto.request.SaveVisitsRequest;
import com.visitas.visitas.visitas.application.dto.response.VisitsResponse;
import com.visitas.visitas.visitas.domain.utils.page.PagedResult;

import java.time.LocalDateTime;

public interface VisitsService {
    VisitsResponse save(SaveVisitsRequest request, Long sellerId);
    PagedResult<VisitsResponse> getVisitsFiltered(
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
