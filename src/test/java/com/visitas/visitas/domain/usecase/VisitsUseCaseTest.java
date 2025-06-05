package com.visitas.visitas.domain.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;

import com.visitas.visitas.visitas.domain.exceptions.InvalidException;
import com.visitas.visitas.visitas.domain.model.VisitsModel;
import com.visitas.visitas.visitas.domain.ports.out.HouseClientPort;
import com.visitas.visitas.visitas.domain.ports.out.VisitsPersistencePort;
import com.visitas.visitas.visitas.domain.usecases.VisitsUseCase;
import com.visitas.visitas.visitas.domain.utils.constants.DomainConstants;
import com.visitas.visitas.visitas.domain.utils.page.PagedResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

class VisitsUseCaseTest {

    private VisitsPersistencePort visitsPersistencePort;
    private HouseClientPort houseClientPort;
    private VisitsUseCase visitsUseCase;

    @BeforeEach
    void setUp() {
        visitsPersistencePort = mock(VisitsPersistencePort.class);
        houseClientPort = mock(HouseClientPort.class);
        visitsUseCase = new VisitsUseCase(visitsPersistencePort, houseClientPort);
    }

    @Test
    void save_shouldThrowWhenHouseDoesNotExist() {
        VisitsModel visit = new VisitsModel(
                1L,
                10L,
                5L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                100L
        )
                ;
        visit.setIdHouse(1L);
        visit.setIdSeller(10L);
        when(houseClientPort.getSellerIdByHouseId(1L)).thenReturn(null);

        InvalidException ex = assertThrows(InvalidException.class, () -> visitsUseCase.save(visit));
        assertEquals(DomainConstants.HOUSE_DOES_NOT_EXIST, ex.getMessage());
    }

    @Test
    void save_shouldThrowWhenHouseNotBelongToSeller() {
        VisitsModel visit = new VisitsModel(
                1L,
                10L,
                5L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                100L
        )
                ;
        visit.setIdHouse(2L);
        visit.setIdSeller(10L);
        when(houseClientPort.getSellerIdByHouseId(2L)).thenReturn(5L);

        InvalidException ex = assertThrows(InvalidException.class, () -> visitsUseCase.save(visit));
        assertEquals(DomainConstants.HOUSE_NOT_BELONG_TO_SELLER, ex.getMessage());
    }

    @Test
    void save_shouldThrowWhenStartDateTimeTooFarInFuture() {
        VisitsModel visit = new VisitsModel(
                1L,
                10L,
                5L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                100L
        )
        ;
        visit.setIdHouse(3L);
        visit.setIdSeller(20L);
        when(houseClientPort.getSellerIdByHouseId(3L)).thenReturn(20L);
        when(houseClientPort.getLocationIdByHouseId(3L)).thenReturn(100L);

        LocalDateTime now = LocalDateTime.now();
        visit.setStartDateTime(now.plusWeeks(DomainConstants.VISIT_WEEKS_RANGE + 1));
        visit.setEndDateTime(now.plusWeeks(DomainConstants.VISIT_WEEKS_RANGE + 2));

        InvalidException ex = assertThrows(InvalidException.class, () -> visitsUseCase.save(visit));
        assertEquals(DomainConstants.VISIT_OUT_OF_ALLOWED_RANGE, ex.getMessage());
    }

    @Test
    void save_shouldThrowWhenStartDateTimeInPast() {
        VisitsModel visit = new VisitsModel(
                1L,
                10L,
                5L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                100L
        )
                ;
        visit.setIdHouse(4L);
        visit.setIdSeller(30L);
        when(houseClientPort.getSellerIdByHouseId(4L)).thenReturn(30L);
        when(houseClientPort.getLocationIdByHouseId(4L)).thenReturn(200L);

        LocalDateTime now = LocalDateTime.now();
        visit.setStartDateTime(now.minusDays(1));
        visit.setEndDateTime(now.plusDays(1));

        InvalidException ex = assertThrows(InvalidException.class, () -> visitsUseCase.save(visit));
        assertEquals(DomainConstants.VISIT_START_IN_PAST, ex.getMessage());
    }

    @Test
    void save_shouldThrowWhenEndBeforeStart() {
        LocalDateTime now = LocalDateTime.now();
        VisitsModel visit = new VisitsModel(
                1L,
                40L,
                5L,
                now.plusDays(2),
                now.plusDays(1),
                300L
        );
        when(houseClientPort.getSellerIdByHouseId(5L)).thenReturn(40L);
        when(houseClientPort.getLocationIdByHouseId(5L)).thenReturn(300L);

        InvalidException ex = assertThrows(InvalidException.class, () -> visitsUseCase.save(visit));
        assertEquals(DomainConstants.VISIT_END_BEFORE_START, ex.getMessage());
    }


    @Test
    void save_shouldThrowWhenOverlapExists() {
        VisitsModel visit = new VisitsModel(
                1L,
                10L,
                5L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                100L
        )
                ;
        visit.setIdHouse(6L);
        visit.setIdSeller(50L);
        when(houseClientPort.getSellerIdByHouseId(6L)).thenReturn(50L);
        when(houseClientPort.getLocationIdByHouseId(6L)).thenReturn(400L);

        LocalDateTime now = LocalDateTime.now();
        visit.setStartDateTime(now.plusDays(1));
        visit.setEndDateTime(now.plusDays(2));
        when(visitsPersistencePort.existsOverlap(50L, visit.getStartDateTime(), visit.getEndDateTime())).thenReturn(true);

        InvalidException ex = assertThrows(InvalidException.class, () -> visitsUseCase.save(visit));
        assertEquals(DomainConstants.VISIT_OVERLAP, ex.getMessage());
    }

    @Test
    void save_shouldReturnSavedVisitWhenValid() {
        VisitsModel visit = new VisitsModel(
                1L,
                10L,
                5L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                100L
        )
                ;
        visit.setIdHouse(7L);
        visit.setIdSeller(60L);
        when(houseClientPort.getSellerIdByHouseId(7L)).thenReturn(60L);
        when(houseClientPort.getLocationIdByHouseId(7L)).thenReturn(500L);

        LocalDateTime now = LocalDateTime.now();
        visit.setStartDateTime(now.plusDays(1));
        visit.setEndDateTime(now.plusDays(2));
        when(visitsPersistencePort.existsOverlap(60L, visit.getStartDateTime(), visit.getEndDateTime())).thenReturn(false);
        when(visitsPersistencePort.save(visit)).thenReturn(visit);

        VisitsModel result = visitsUseCase.save(visit);
        assertSame(visit, result);
        assertEquals(500L, visit.getLocationId());
    }

    @Test
    void getFilters_shouldReturnPagedResult() {
        Integer page = 0, size = 5;
        LocalDateTime startFrom = LocalDateTime.now().minusDays(1);
        LocalDateTime startTo = LocalDateTime.now().plusDays(1);
        LocalDateTime endFrom = null, endTo = null;
        Long locationId = 1000L;
        String sortBy = "startDateTime";
        boolean orderAsc = true;

        VisitsModel v1 = new VisitsModel(
                1L,
                10L,
                5L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                100L
        )
                ;
        VisitsModel v2 = new VisitsModel(
                2L,
                9L,
                4L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                10L
        )
        ;
        List<VisitsModel> list = List.of(v1, v2);
        Page<VisitsModel> pageResult = new PageImpl<>(list, PageRequest.of(page, size, Sort.by(sortBy).ascending()), list.size());

        when(visitsPersistencePort.findWithFilters(
                eq(startFrom),
                eq(startTo),
                eq(endFrom),
                eq(endTo),
                eq(locationId),
                any(LocalDateTime.class),
                eq(2),
                any(PageRequest.class)
        )).thenReturn(pageResult);

        PagedResult<VisitsModel> result = visitsUseCase.getFilters(page, size, startFrom, startTo, endFrom, endTo, locationId, sortBy, orderAsc);

        assertEquals(list, result.getContent());
        assertEquals(page, result.getPage());
        assertEquals(size, result.getSize());
        assertEquals(list.size(), result.getTotalElements());
    }
}

