package com.visitas.visitas.visitas.application.services.impl;

import com.visitas.visitas.visitas.application.dto.request.SaveVisitsRequest;
import com.visitas.visitas.visitas.application.dto.response.VisitsResponse;
import com.visitas.visitas.visitas.application.mappers.VisitsDtoMapper;
import com.visitas.visitas.visitas.application.services.VisitsService;
import com.visitas.visitas.visitas.domain.model.VisitsModel;
import com.visitas.visitas.visitas.domain.ports.in.VisitsServicePort;
import com.visitas.visitas.visitas.domain.utils.page.PagedResult;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VisitsServiceImpl implements VisitsService {

    private final VisitsServicePort visitsServicePort;
    private final VisitsDtoMapper   mapper;

    public VisitsServiceImpl(VisitsServicePort visitsServicePort,
                             VisitsDtoMapper mapper) {
        this.visitsServicePort = visitsServicePort;
        this.mapper            = mapper;
    }

    @Override
    @Transactional
    public VisitsResponse save(SaveVisitsRequest dto, Long sellerId) {
        VisitsModel model = mapper.requestToModel(dto);
        model.setIdSeller(sellerId);
        VisitsModel saved = visitsServicePort.save(model);
        return mapper.modelToResponse(saved);
    }

    @Override
    public PagedResult<VisitsResponse> getVisitsFiltered(
            Integer page,
            Integer size,
            LocalDateTime startFrom,
            LocalDateTime startTo,
            LocalDateTime endFrom,
            LocalDateTime endTo,
            Long locationId,
            String sortBy,
            boolean orderAsc
    ) {
        PagedResult<VisitsModel> domainPage = visitsServicePort.getFilters(
                page, size,
                startFrom, startTo,
                endFrom, endTo,
                locationId,
                sortBy, orderAsc
        );

        List<VisitsResponse> dtos = domainPage.getContent().stream()
                .map(mapper::modelToResponse)
                .toList();

        return new PagedResult<>(
                dtos,
                domainPage.getPage(),
                domainPage.getSize(),
                domainPage.getTotalElements()
        );
    }
}


