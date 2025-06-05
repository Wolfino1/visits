package com.visitas.visitas.visitas.infrastructure.adapters.persistence.mysql;

import com.visitas.visitas.visitas.domain.model.VisitsModel;

import com.visitas.visitas.visitas.domain.ports.out.VisitsPersistencePort;
import com.visitas.visitas.visitas.infrastructure.entities.VisitsEntity;
import com.visitas.visitas.visitas.infrastructure.mappers.VisitsEntityMapper;
import com.visitas.visitas.visitas.infrastructure.repositories.mysql.VisitsRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VisitsPersistenceAdapter implements VisitsPersistencePort {

    private final VisitsRepository repository;
    private final VisitsEntityMapper entityMapper;


    public VisitsPersistenceAdapter(VisitsRepository repository,
                                    VisitsEntityMapper entityMapper) {
        this.repository   = repository;
        this.entityMapper = entityMapper;
    }

    @Override
    public VisitsModel save(VisitsModel visit) {
        VisitsEntity entity = entityMapper.modelToEntity(visit);
        VisitsEntity saved  = repository.save(entity);
        VisitsModel result  = entityMapper.entityToModel(saved);
        result.setLocationId(saved.getLocationId());
        return result;
    }

    @Override
    public boolean existsOverlap(Long sellerId,
                                 LocalDateTime startDateTime,
                                 LocalDateTime endDateTime) {
        return repository.existsOverlap(sellerId, startDateTime, endDateTime);
    }


    @Override
    public Long getLocationId(Long visitId) {
        return repository.findById(visitId)
                .map(VisitsEntity::getLocationId)
                .orElse(null);
    }

    @Override
    public Page<VisitsModel> findWithFilters(
            LocalDateTime startFrom,
            LocalDateTime startTo,
            LocalDateTime endFrom,
            LocalDateTime endTo,
            Long locationId,
            LocalDateTime now,
            int maxScheduled,
            Pageable pageable
    ) {
        Specification<VisitsEntity> spec = (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();

            if (startFrom != null)
                preds.add(cb.greaterThanOrEqualTo(root.get("startDateTime"), startFrom));
            if (startTo != null)
                preds.add(cb.lessThanOrEqualTo(root.get("startDateTime"), startTo));

            if (endFrom != null)
                preds.add(cb.greaterThanOrEqualTo(root.get("endDateTime"), endFrom));
            if (endTo != null)
                preds.add(cb.lessThanOrEqualTo(root.get("endDateTime"), endTo));

            if (locationId != null)
                preds.add(cb.equal(root.get("location").get("id"), locationId));

            preds.add(cb.greaterThanOrEqualTo(root.get("startDateTime"), now));

            return cb.and(preds.toArray(new Predicate[0]));
        };

        Page<VisitsEntity> page = repository.findAll(spec, pageable);
        return page.map(entityMapper::entityToModel);
    }

    @Override
    public Optional<VisitsModel> findById(Long id) {
        return repository.findById(id)
                .map(entityMapper::entityToModel);
    }
}

