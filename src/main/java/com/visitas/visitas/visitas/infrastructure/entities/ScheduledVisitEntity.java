package com.visitas.visitas.visitas.infrastructure.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "scheduled_visits",
        uniqueConstraints = @UniqueConstraint(columnNames = { "visits_id", "buyer_id" })
)
public class ScheduledVisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = VisitsEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "visits_id")
    private VisitsEntity visit;

    @Column(name = "buyer_id", nullable = false)
    private Long buyerId;
}
