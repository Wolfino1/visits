package com.visitas.visitas.visitas.infrastructure.adapters.persistence.mysql;

import com.visitas.visitas.visitas.domain.model.ScheduledVisitModel;
import com.visitas.visitas.visitas.domain.ports.out.ScheduledVisitPersistencePort;
import com.visitas.visitas.visitas.infrastructure.entities.ScheduledVisitEntity;
import com.visitas.visitas.visitas.infrastructure.mappers.ScheduledVisitEntityMapper;
import com.visitas.visitas.visitas.infrastructure.repositories.mysql.ScheduledVisitsRepository;
import org.springframework.transaction.annotation.Transactional;

public class ScheduledVisitPersistenceAdapter implements ScheduledVisitPersistencePort {

    private final ScheduledVisitsRepository repository;
    private final ScheduledVisitEntityMapper entityMapper;


    public ScheduledVisitPersistenceAdapter(ScheduledVisitsRepository repository,ScheduledVisitEntityMapper entityMapper) {
        this.repository = repository;
        this.entityMapper = entityMapper;
    }

    @Override
    @Transactional
    public ScheduledVisitModel save(ScheduledVisitModel model) {
        ScheduledVisitEntity entity = entityMapper.modelToEntity(model);

        ScheduledVisitEntity saved = repository.save(entity);

        return entityMapper.entityToModel(saved);
    }

    @Override
    public int countByVisitId(Long visitId) {
        return repository.countByVisit_Id(visitId);
    }

    @Override
    public boolean existsByVisitIdAndBuyerId(Long visitId, Long buyerId) {
        return repository.existsByVisit_IdAndBuyerId(visitId, buyerId);
    }
}
