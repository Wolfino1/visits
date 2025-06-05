package com.visitas.visitas.visitas.infrastructure.mappers;
import com.visitas.visitas.visitas.domain.model.ScheduledVisitModel;
import com.visitas.visitas.visitas.infrastructure.entities.ScheduledVisitEntity;
import com.visitas.visitas.visitas.infrastructure.entities.VisitsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ScheduledVisitEntityMapper {

    @Mapping(source = "visitId", target = "visit", qualifiedByName = "visitIdToEntity")
    @Mapping(source = "buyerId", target = "buyerId")
    ScheduledVisitEntity modelToEntity(ScheduledVisitModel model);

    @Mapping(source = "id",      target = "id")
    @Mapping(source = "visit.id", target = "visitId")
    @Mapping(source = "buyerId", target = "buyerId")
    ScheduledVisitModel entityToModel(ScheduledVisitEntity entity);

    @Named("visitIdToEntity")
    default VisitsEntity visitIdToEntity(Long visitId) {
        if (visitId == null) {
            return null;
        }
        VisitsEntity ve = new VisitsEntity();
        ve.setId(visitId);
        return ve;
    }
}

