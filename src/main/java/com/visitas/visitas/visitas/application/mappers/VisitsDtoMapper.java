package com.visitas.visitas.visitas.application.mappers;


import com.visitas.visitas.visitas.application.dto.request.SaveVisitsRequest;
import com.visitas.visitas.visitas.application.dto.response.VisitsResponse;
import com.visitas.visitas.visitas.domain.model.VisitsModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class VisitsDtoMapper {

    // Request → Model
    @Mapping(source = "houseId",       target = "idHouse")
    @Mapping(source = "startDateTime", target = "startDateTime")
    @Mapping(source = "endDateTime",   target = "endDateTime")
    public abstract VisitsModel requestToModel(SaveVisitsRequest request);

    // Model → Response
    @Mapping(source = "id",                   target = "id")
    @Mapping(source = "idSeller",             target = "sellerId")
    @Mapping(source = "idHouse",              target = "houseId")
    @Mapping(source = "startDateTime",        target = "startDateTime")
    @Mapping(source = "endDateTime",          target = "endDateTime")
    @Mapping(source = "scheduledPersonCount", target = "scheduledPersonCount")
    @Mapping(source = "locationId",           target = "locationId")
    public abstract VisitsResponse modelToResponse(VisitsModel model);
}

