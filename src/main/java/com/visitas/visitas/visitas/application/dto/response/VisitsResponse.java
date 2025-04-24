package com.visitas.visitas.visitas.application.dto.response;

import java.time.LocalDateTime;

public record VisitsResponse(Long id,
                             Long sellerId,
                             Long houseId,
                             LocalDateTime startDateTime,
                             LocalDateTime endDateTime,
                             int scheduledPersonCount,
                             Long locationId) {
}
