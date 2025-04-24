package com.visitas.visitas.visitas.infrastructure.mappers;

import com.visitas.visitas.visitas.domain.model.VisitsModel;
import com.visitas.visitas.visitas.infrastructure.entities.VisitsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VisitsEntityMapper {
    @Mapping(source="idSeller", target="sellerId")
    @Mapping(source="idHouse",  target="houseId")
    VisitsEntity modelToEntity(VisitsModel m);

    @Mapping(source="sellerId", target="idSeller")
    @Mapping(source="houseId",  target="idHouse")
    VisitsModel entityToModel(VisitsEntity e);
}


