package com.visitas.visitas.commonconfigurations.beans;

import com.visitas.visitas.visitas.domain.ports.in.ScheduledVisitServicePort;
import com.visitas.visitas.visitas.domain.ports.in.VisitsServicePort;
import com.visitas.visitas.visitas.domain.ports.out.HouseClientPort;
import com.visitas.visitas.visitas.domain.ports.out.ScheduledVisitPersistencePort;
import com.visitas.visitas.visitas.domain.ports.out.VisitsPersistencePort;
import com.visitas.visitas.visitas.domain.usecases.ScheduledVisitUseCase;
import com.visitas.visitas.visitas.domain.usecases.VisitsUseCase;
import com.visitas.visitas.visitas.infrastructure.adapters.persistence.mysql.ScheduledVisitPersistenceAdapter;
import com.visitas.visitas.visitas.infrastructure.adapters.persistence.mysql.VisitsPersistenceAdapter;
import com.visitas.visitas.visitas.infrastructure.mappers.ScheduledVisitEntityMapper;
import com.visitas.visitas.visitas.infrastructure.mappers.VisitsEntityMapper;
import com.visitas.visitas.visitas.infrastructure.repositories.mysql.ScheduledVisitsRepository;
import com.visitas.visitas.visitas.infrastructure.repositories.mysql.VisitsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;


@Configuration
@RequiredArgsConstructor

public class BeanConfiguration {

    private final VisitsRepository visitsRepository;
    private final VisitsEntityMapper visitsEntityMapper;
    private final HouseClientPort houseClientPort;
    private final ScheduledVisitsRepository scheduledVisitsRepository;
    private final ScheduledVisitEntityMapper scheduledVisitEntityMapper;


    @Bean
    public VisitsPersistencePort visitsPersistencePort() {
        return new VisitsPersistenceAdapter(visitsRepository, visitsEntityMapper);
    }

    @Bean
    public ScheduledVisitPersistencePort scheduledVisitPersistencePort() {
        return new ScheduledVisitPersistenceAdapter(scheduledVisitsRepository, scheduledVisitEntityMapper);
    }

    @Bean
    @Transactional
    public ScheduledVisitServicePort scheduledVisitServicePort(
            ScheduledVisitPersistencePort scheduledVisitPersistencePort,
            VisitsPersistencePort visitsPersistencePort) {
        return new ScheduledVisitUseCase(scheduledVisitPersistencePort, visitsPersistencePort);
    }

    @Bean
    @Transactional
    public VisitsServicePort visitsServicePort(
            VisitsPersistencePort visitsPersistencePort,
            HouseClientPort houseClientPort) {
        return new VisitsUseCase(visitsPersistencePort, houseClientPort);
    }
}
