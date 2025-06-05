package com.visitas.visitas.visitas.infrastructure.repositories.mysql;

import com.visitas.visitas.visitas.infrastructure.entities.ScheduledVisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduledVisitsRepository extends JpaRepository<ScheduledVisitEntity, Long> {
    int countByVisit_Id(Long visitId);
    boolean existsByVisit_IdAndBuyerId(Long visitId, Long buyerId);
}