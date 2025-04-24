package com.visitas.visitas.visitas.infrastructure.repositories.mysql;

import com.visitas.visitas.visitas.infrastructure.entities.VisitsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface VisitsRepository extends JpaRepository<VisitsEntity, Long>, JpaSpecificationExecutor<VisitsEntity> {

    @Query("""
      SELECT v
      FROM VisitsEntity v
      WHERE (:startFrom     IS NULL OR v.startDateTime >= :startFrom)
        AND (:startTo       IS NULL OR v.startDateTime <= :startTo)
        AND (:endFrom       IS NULL OR v.endDateTime   >= :endFrom)
        AND (:endTo         IS NULL OR v.endDateTime   <= :endTo)
        AND (:locationId    IS NULL OR v.locationId    = :locationId)
    """)
    Page<VisitsEntity> findWithFilters(
        @Param("startFrom")              LocalDateTime startFrom,
            @Param("startTo")            LocalDateTime startTo,
            @Param("endFrom")            LocalDateTime endFrom,
            @Param("endTo")              LocalDateTime endTo,
            @Param("locationId")         Long locationId,
            Pageable pageable
    );

    @Query("""
      SELECT CASE WHEN COUNT(v) > 0 THEN TRUE ELSE FALSE END
      FROM VisitsEntity v
      WHERE v.sellerId = :sellerId
        AND v.startDateTime < :endDateTime
        AND v.endDateTime   > :startDateTime
    """)
    boolean existsOverlap(
            @Param("sellerId")       Long sellerId,
            @Param("startDateTime")  LocalDateTime startDateTime,
            @Param("endDateTime")    LocalDateTime endDateTime
    );
}
