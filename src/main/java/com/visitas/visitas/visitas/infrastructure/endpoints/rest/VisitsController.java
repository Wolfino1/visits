package com.visitas.visitas.visitas.infrastructure.endpoints.rest;


import com.visitas.visitas.visitas.application.dto.request.SaveVisitsRequest;
import com.visitas.visitas.visitas.application.dto.response.VisitsResponse;
import com.visitas.visitas.visitas.application.mappers.VisitsDtoMapper;
import com.visitas.visitas.visitas.application.services.VisitsService;
import com.visitas.visitas.visitas.domain.model.VisitsModel;
import com.visitas.visitas.visitas.domain.usecases.VisitsUseCase;
import com.visitas.visitas.visitas.domain.utils.page.PagedResult;
import com.visitas.visitas.visitas.infrastructure.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/visits")
@RequiredArgsConstructor
@Tag(name = "Visits", description = "Controller for Visits")
public class VisitsController {

    private final VisitsService visitsService;
    private final JwtUtil jwtUtil;
    private final VisitsUseCase visitsUseCase;
    private final VisitsDtoMapper dtoMapper;

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/")
    @Operation(
            summary     = "Create a visit",
            description = "Create a new visit slot for a house",
            tags        = {"Visits"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Visit creation payload",
                    required    = true,
                    content     = @Content(
                            mediaType = "application/json",
                            schema    = @Schema(implementation = SaveVisitsRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description  = "Visit created successfully",
                            content      = @Content(
                                    mediaType = "application/json",
                                    schema    = @Schema(implementation = VisitsResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<VisitsResponse> save(
            @RequestBody SaveVisitsRequest dto,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.substring(7);

        Long sellerId = jwtUtil.extractSellerId(token);

        VisitsResponse resp = visitsService.save(dto, sellerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }


    @GetMapping("/get")
    @Operation(
            summary     = "Get visits with filters",
            description = "Retrieve a page of upcoming visits filtered by date/time ranges and location, ordered by startDateTime DESC by default",
            tags        = {"Visits"}
    )
    public ResponseEntity<PagedResult<VisitsResponse>> getVisitsFiltered(
            @Parameter(description = "Zero-based page index", required = true)
            @RequestParam Integer page,

            @Parameter(description = "Page size", required = true)
            @RequestParam Integer size,

            @Parameter(description = "Start date/time from (inclusive)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startFrom,

            @Parameter(description = "Start date/time to (inclusive)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTo,

            @Parameter(description = "End date/time from (inclusive)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endFrom,

            @Parameter(description = "End date/time to (inclusive)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTo,

            @Parameter(description = "Filter by location ID")
            @RequestParam(required = false)
            Long locationId,

            @Parameter(description = "Field to sort by (e.g. startDateTime)")
            @RequestParam(required = false, defaultValue = "startDateTime")
            String sortBy,

            @Parameter(description = "Sort ascending if true, descending if false (default: false)")
            @RequestParam(defaultValue = "false")
            boolean orderAsc
    ) {
        PagedResult<VisitsModel> modelos = visitsUseCase.getFilters(
                page, size,
                startFrom, startTo,
                endFrom, endTo,
                locationId,
                sortBy, orderAsc
        );

        List<VisitsResponse> resp = modelos.getContent()
                .stream()
                .map(dtoMapper::modelToResponse)
                .toList();

        PagedResult<VisitsResponse> pageResp = new PagedResult<>(
                resp,
                modelos.getPage(),
                modelos.getSize(),
                modelos.getTotalElements()
        );

        return ResponseEntity.ok(pageResp);
    }
}

