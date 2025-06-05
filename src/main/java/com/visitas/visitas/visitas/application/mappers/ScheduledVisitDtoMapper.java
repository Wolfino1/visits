package com.visitas.visitas.visitas.application.mappers;

import com.visitas.visitas.visitas.application.dto.request.SaveScheduledVisitRequest;
import com.visitas.visitas.visitas.application.dto.response.ScheduledVisitResponse;
import com.visitas.visitas.visitas.domain.model.ScheduledVisitModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ScheduledVisitDtoMapper {

    @Mapping(source = "visitId", target = "visitId")
    public abstract ScheduledVisitModel requestToModel(SaveScheduledVisitRequest request);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "visitId", target = "visitId")
    @Mapping(source = "buyerId", target = "buyerId")
    public abstract ScheduledVisitResponse modelToResponse(ScheduledVisitModel model);
}
