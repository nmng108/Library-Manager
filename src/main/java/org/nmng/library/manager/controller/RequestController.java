package org.nmng.library.manager.controller;

import jakarta.validation.Valid;
import org.nmng.library.manager.dto.request.ChangeRequestStatusDto;
import org.nmng.library.manager.dto.request.CreateRequestDto;
import org.nmng.library.manager.entity.RequestStatus;
import org.nmng.library.manager.model.Policy;
import org.nmng.library.manager.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping({"/api/requests", "/api/requests/"})
public class RequestController {
    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(name = "patron", required = false) String patronIdentifiable,
            @RequestParam(name = "status", required = false) String statusName
    ) {
        if (patronIdentifiable == null && statusName == null) return this.requestService.getAll();
        return this.requestService.getByPatronAndStatus(patronIdentifiable, statusName);
    }

    @GetMapping({"/{identifiable}", "/{identifiable}/"})
    public ResponseEntity<?> getSpecified(@PathVariable String identifiable) {
        return this.requestService.getSpecifiedOne(identifiable);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateRequestDto dto) {
        return this.requestService.create(dto);
    }

    @PatchMapping
    public ResponseEntity<?> changeStatus(@RequestBody @Valid ChangeRequestStatusDto dto) {
        return switch (dto.getStatus().toUpperCase()) {
            case RequestStatus.RETURNED -> this.requestService.completeRequest(dto.getId());
            case RequestStatus.CANCELLED -> this.requestService.cancelRequest(dto.getId());
            default -> ResponseEntity.badRequest().body("unknown status");
        };
    }

    @DeleteMapping({"/{identifiable}", "/{identifiable}/"})
    public ResponseEntity<?> delete(@PathVariable String identifiable) {
        return this.requestService.delete(identifiable);
    }

    @PatchMapping("/policies")
    public ResponseEntity<?> updatePolicies(@RequestParam(name = "fine", required = false) Double finePerInterval,
                                            @RequestParam(name = "interval", required = false) Long interval) {
        if (finePerInterval != null) Policy.FINE_PER_INTERVAL = finePerInterval;
        if (interval != null) Policy.FINE_COUNTING_INTERVAL = Duration.of(interval, ChronoUnit.SECONDS);
        return ResponseEntity.ok().build();
    }
}
