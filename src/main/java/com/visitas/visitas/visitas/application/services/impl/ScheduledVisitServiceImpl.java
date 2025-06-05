package com.visitas.visitas.visitas.application.services.impl;

import com.visitas.visitas.visitas.application.dto.request.SaveScheduledVisitRequest;
import com.visitas.visitas.visitas.application.dto.response.ScheduledVisitResponse;
import com.visitas.visitas.visitas.application.mappers.ScheduledVisitDtoMapper;
import com.visitas.visitas.visitas.application.services.ScheduledVisitService;
import com.visitas.visitas.visitas.domain.model.ScheduledVisitModel;
import com.visitas.visitas.visitas.domain.ports.in.ScheduledVisitServicePort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ScheduledVisitServiceImpl implements ScheduledVisitService {

    private final ScheduledVisitServicePort scheduledvisitServicePort;
    private final ScheduledVisitDtoMapper mapper;

    public ScheduledVisitServiceImpl(ScheduledVisitServicePort scheduledvisitServicePort,
                             ScheduledVisitDtoMapper mapper) {
        this.scheduledvisitServicePort = scheduledvisitServicePort;
        this.mapper            = mapper;
    }

    @Override
    @Transactional
    public ScheduledVisitResponse save(SaveScheduledVisitRequest dto, Long sellerId) {
        ScheduledVisitModel model = mapper.requestToModel(dto);
        model.setBuyerId(sellerId);
        ScheduledVisitModel saved = scheduledvisitServicePort.save(model);
        return mapper.modelToResponse(saved);
    }
}

