package com.visitas.visitas.visitas.application.dto.request;

import java.time.LocalDateTime;

public record SaveVisitsRequest(
                                Long houseId,
                                LocalDateTime startDateTime,
                                LocalDateTime endDateTime) {
}
