package com.visitas.visitas.visitas.infrastructure.endpoints.rest;

import com.visitas.visitas.visitas.application.dto.request.SaveScheduledVisitRequest;
import com.visitas.visitas.visitas.application.dto.response.ScheduledVisitResponse;
import com.visitas.visitas.visitas.application.services.ScheduledVisitService;
import com.visitas.visitas.visitas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/visits")
public class ScheduleVisitController {

    private final JwtUtil jwtUtil;
    private final ScheduledVisitService scheduledVisitService;

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/schedule")
    public ResponseEntity<ScheduledVisitResponse> schedule(
            @RequestBody SaveScheduledVisitRequest dto,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);
        Long buyerId = jwtUtil.extractBuyerId(token);

        ScheduledVisitResponse resp = scheduledVisitService.save(dto, buyerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }
}



