package com.visitas.visitas.visitas.domain.usecases;

import com.visitas.visitas.visitas.domain.exceptions.InvalidException;
import com.visitas.visitas.visitas.domain.model.VisitsModel;
import com.visitas.visitas.visitas.domain.ports.in.VisitsServicePort;
import com.visitas.visitas.visitas.domain.ports.out.HouseClientPort;
import com.visitas.visitas.visitas.domain.ports.out.VisitsPersistencePort;
import com.visitas.visitas.visitas.domain.utils.constants.DomainConstants;
import com.visitas.visitas.visitas.domain.utils.page.PagedResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

public class VisitsUseCase implements VisitsServicePort {

    private final VisitsPersistencePort visitsPersistencePort;
    private final HouseClientPort houseClientPort;

    public VisitsUseCase(VisitsPersistencePort visitsPersistencePort,
                         HouseClientPort houseClientPort) {
        this.visitsPersistencePort = visitsPersistencePort;
        this.houseClientPort = houseClientPort;
    }

    @Override
    public VisitsModel save(VisitsModel visit) {
        Long actualSeller = houseClientPort.getSellerIdByHouseId(visit.getIdHouse());
        if (actualSeller == null) {
            throw new InvalidException(DomainConstants.HOUSE_DOES_NOT_EXIST);
        }
        if (!actualSeller.equals(visit.getIdSeller())) {
            throw new InvalidException(DomainConstants.HOUSE_NOT_BELONG_TO_SELLER);
        }

        Long locationId = houseClientPort.getLocationIdByHouseId(visit.getIdHouse());
        visit.setLocationId(locationId);

        LocalDateTime now = LocalDateTime.now();
        if (visit.getStartDateTime().isAfter(now.plusWeeks(DomainConstants.VISIT_WEEKS_RANGE))) {
            throw new InvalidException(DomainConstants.VISIT_OUT_OF_ALLOWED_RANGE);
        }
        if (visit.getStartDateTime().isBefore(now)) {
            throw new InvalidException(DomainConstants.VISIT_START_IN_PAST);
        }
        if (visit.getEndDateTime().isBefore(visit.getStartDateTime())) {
            throw new InvalidException(DomainConstants.VISIT_END_BEFORE_START);
        }
        boolean overlap = visitsPersistencePort.existsOverlap(
                visit.getIdSeller(),
                visit.getStartDateTime(),
                visit.getEndDateTime()
        );
        if (overlap) {
            throw new InvalidException(DomainConstants.VISIT_OVERLAP);
        }

        return visitsPersistencePort.save(visit);
    }

    @Override
    public PagedResult<VisitsModel> getFilters(
            Integer page,
            Integer size,
            LocalDateTime startFrom,
            LocalDateTime startTo,
            LocalDateTime endFrom,
            LocalDateTime endTo,
            Long locationId,
            String sortBy,
            boolean orderAsc
    ) {
        Sort sort = orderAsc
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        LocalDateTime now = LocalDateTime.now();
        int maxScheduled = 2;

        Page<VisitsModel> pageResult = visitsPersistencePort.findWithFilters(
                startFrom,
                startTo,
                endFrom,
                endTo,
                locationId,
                now,
                maxScheduled,
                pageable
        );

        return new PagedResult<>(
                pageResult.getContent(),
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements()
        );
    }
}