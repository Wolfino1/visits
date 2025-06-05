package com.visitas.visitas.visitas.infrastructure.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "visits")
public class VisitsEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seller_id",       nullable = false)
    private Long sellerId;

    @Column(name = "house_id",        nullable = false)
    private Long houseId;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time",   nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "location_id")
    private Long locationId;

}

