package com.visitas.visitas.domain.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import com.visitas.visitas.visitas.domain.exceptions.InvalidException;
import com.visitas.visitas.visitas.domain.model.ScheduledVisitModel;
import com.visitas.visitas.visitas.domain.model.VisitsModel;
import com.visitas.visitas.visitas.domain.ports.out.ScheduledVisitPersistencePort;
import com.visitas.visitas.visitas.domain.ports.out.VisitsPersistencePort;
import com.visitas.visitas.visitas.domain.usecases.ScheduledVisitUseCase;
import com.visitas.visitas.visitas.domain.utils.constants.DomainConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ScheduledVisitUseCaseTest {

    private ScheduledVisitPersistencePort scheduledVisitPersistencePort;
    private VisitsPersistencePort visitsPersistencePort;
    private ScheduledVisitUseCase scheduledVisitUseCase;

    @BeforeEach
    void setUp() {
        scheduledVisitPersistencePort = mock(ScheduledVisitPersistencePort.class);
        visitsPersistencePort = mock(VisitsPersistencePort.class);
        scheduledVisitUseCase = new ScheduledVisitUseCase(scheduledVisitPersistencePort, visitsPersistencePort);
    }

    @Test
    void save_shouldThrowWhenVisitNotFound() {
        ScheduledVisitModel sv = new ScheduledVisitModel();
        sv.setVisitId(1L);
        sv.setBuyerId(10L);
        when(visitsPersistencePort.findById(1L)).thenReturn(Optional.empty());

        InvalidException ex = assertThrows(InvalidException.class, () -> scheduledVisitUseCase.save(sv));
        assertEquals(DomainConstants.VISIT_NOT_FOUND, ex.getMessage());
    }

    @Test
    void save_shouldThrowWhenVisitStartInPast() {
        VisitsModel visit = new VisitsModel(
                1L,
                10L,
                5L,
                LocalDateTime.of(2025, 6, 10, 14, 0),
                LocalDateTime.of(2025, 6, 10, 15, 0),
                100L
        );
        visit.setStartDateTime(LocalDateTime.now().minusDays(1));
        when(visitsPersistencePort.findById(2L)).thenReturn(Optional.of(visit));

        ScheduledVisitModel sv = new ScheduledVisitModel();
        sv.setVisitId(2L);
        sv.setBuyerId(20L);

        InvalidException ex = assertThrows(InvalidException.class, () -> scheduledVisitUseCase.save(sv));
        assertEquals(DomainConstants.VISIT_START_IN_PAST, ex.getMessage());
    }

    @Test
    void save_shouldThrowWhenSlotFullByMaxConstant() {
        VisitsModel visit = new VisitsModel(
                1L,
                10L,
                5L,
                LocalDateTime.of(2025, 6, 10, 14, 0),
                LocalDateTime.of(2025, 6, 10, 15, 0),
                100L
        );
        visit.setStartDateTime(LocalDateTime.now().plusDays(1));
        when(visitsPersistencePort.findById(3L)).thenReturn(Optional.of(visit));
        when(scheduledVisitPersistencePort.countByVisitId(3L)).thenReturn(DomainConstants.MAX_SCHEDULED_PER_SLOT);

        ScheduledVisitModel sv = new ScheduledVisitModel();
        sv.setVisitId(3L);
        sv.setBuyerId(30L);

        InvalidException ex = assertThrows(InvalidException.class, () -> scheduledVisitUseCase.save(sv));
        assertEquals(DomainConstants.VISIT_SLOT_FULL, ex.getMessage());
    }

    @Test
    void save_shouldThrowWhenVisitFullByCount() {
        VisitsModel visit = new VisitsModel(
                1L,
                10L,
                5L,
                LocalDateTime.of(2025, 6, 10, 14, 0),
                LocalDateTime.of(2025, 6, 10, 15, 0),
                100L
        );
        visit.setStartDateTime(LocalDateTime.now().plusDays(1));
        when(visitsPersistencePort.findById(4L)).thenReturn(Optional.of(visit));
        when(scheduledVisitPersistencePort.countByVisitId(4L))
                .thenReturn(DomainConstants.MAX_SCHEDULED_PER_SLOT - 1, 2);
        when(scheduledVisitPersistencePort.existsByVisitIdAndBuyerId(4L, 40L))
                .thenReturn(false);

        ScheduledVisitModel sv = new ScheduledVisitModel();
        sv.setVisitId(4L);
        sv.setBuyerId(40L);

        InvalidException ex = assertThrows(InvalidException.class,
                () -> scheduledVisitUseCase.save(sv)
        );
        assertEquals(DomainConstants.VISIT_FULL, ex.getMessage());
    }

    @Test
    void save_shouldThrowWhenAlreadyScheduledByUser() {
        VisitsModel visit = new VisitsModel(
                1L,
                10L,
                5L,
                LocalDateTime.of(2025, 6, 10, 14, 0),
                LocalDateTime.of(2025, 6, 10, 15, 0),
                100L
        );
        visit.setStartDateTime(LocalDateTime.now().plusDays(1));
        when(visitsPersistencePort.findById(5L)).thenReturn(Optional.of(visit));
        when(scheduledVisitPersistencePort.countByVisitId(5L)).thenReturn(0);
        when(scheduledVisitPersistencePort.existsByVisitIdAndBuyerId(5L, 50L)).thenReturn(true);

        ScheduledVisitModel sv = new ScheduledVisitModel();
        sv.setVisitId(5L);
        sv.setBuyerId(50L);

        InvalidException ex = assertThrows(InvalidException.class, () -> scheduledVisitUseCase.save(sv));
        assertEquals(DomainConstants.VISIT_ALREADY_SCHEDULED_BY_USER, ex.getMessage());
    }

    @Test
    void save_shouldReturnWhenValid() {
        VisitsModel visit = new VisitsModel(
                1L,
                10L,
                5L,
                LocalDateTime.of(2025, 6, 10, 14, 0),
                LocalDateTime.of(2025, 6, 10, 15, 0),
                100L
        );
        visit.setStartDateTime(LocalDateTime.now().plusDays(1));
        when(visitsPersistencePort.findById(6L)).thenReturn(Optional.of(visit));
        when(scheduledVisitPersistencePort.countByVisitId(6L)).thenReturn(0);
        when(scheduledVisitPersistencePort.existsByVisitIdAndBuyerId(6L, 60L)).thenReturn(false);
        ScheduledVisitModel sv = new ScheduledVisitModel();
        sv.setVisitId(6L);
        sv.setBuyerId(60L);
        when(scheduledVisitPersistencePort.save(sv)).thenReturn(sv);

        ScheduledVisitModel result = scheduledVisitUseCase.save(sv);
        assertSame(sv, result);
    }
}
