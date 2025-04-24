package com.visitas.visitas.visitas.domain.model;

import com.visitas.visitas.visitas.domain.exceptions.InvalidException;
import com.visitas.visitas.visitas.domain.exceptions.NullException;
import com.visitas.visitas.visitas.domain.utils.constants.DomainConstants;

import java.time.LocalDateTime;

public class VisitsModel {
    private Long id;
    private Long idSeller;
    private Long idHouse;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private int scheduledPersonCount;
    private Long locationId;

    public VisitsModel(Long id, Long idSeller, Long idHouse,
                       LocalDateTime startDateTime, LocalDateTime endDateTime,
                       int scheduledPersonCount, Long locationId) {
        this.id = id;
        this.idSeller = idSeller;
        this.idHouse = idHouse;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.scheduledPersonCount = scheduledPersonCount;
        this.locationId = locationId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdSeller() {
        return idSeller;
    }

    public void setIdSeller(Long idSeller) {
        this.idSeller = idSeller;
    }

    public Long getIdHouse() {
        return idHouse;
    }

    public void setIdHouse(Long idHouse) {
        this.idHouse = idHouse;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime start) {
        if (start == null) throw new NullException(DomainConstants.START_DATE_NULL_MESSAGE);
        this.startDateTime = start;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime end) {
        if (end == null) throw new NullException(DomainConstants.END_DATE_NULL_MESSAGE);
        if (this.startDateTime != null && end.isBefore(this.startDateTime))
            throw new InvalidException(DomainConstants.END_DATE_MUST_BE_AFTER_START_DATE);
        this.endDateTime = end;
    }
    public int getScheduledPersonCount() {
        return scheduledPersonCount;
    }
    public void setScheduledPersonCount(int scheduledPersonCount) {
        this.scheduledPersonCount = scheduledPersonCount;
    }

    public Long getLocationId() {
        return locationId;
    }
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }
}
