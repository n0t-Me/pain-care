package com.pain_care.pain_care.rest;

import com.pain_care.pain_care.model.DiagnosticDTO;
import com.pain_care.pain_care.service.DiagnosticService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/diagnostics", produces = MediaType.APPLICATION_JSON_VALUE)
public class DiagnosticResource {

    private final DiagnosticService diagnosticService;

    public DiagnosticResource(final DiagnosticService diagnosticService) {
        this.diagnosticService = diagnosticService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<DiagnosticDTO> getDiagnostic(@PathVariable(name = "userId") final Integer userId) {
        return ResponseEntity.ok(diagnosticService.getLatestDiagnostic(userId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createDiagnostic(@RequestBody @Valid final DiagnosticDTO diagnosticDTO) {
        final Integer createdId = diagnosticService.create(diagnosticDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Integer> updateDiagnostic(
            @PathVariable(name = "userId") final Integer userId,
            @RequestBody @Valid final DiagnosticDTO diagnosticDTO) {
        diagnosticService.update(userId, diagnosticDTO);
        return ResponseEntity.ok(userId);
    }

    @DeleteMapping("/{userId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteDiagnostic(@PathVariable(name = "userId") final Integer userId) {
        diagnosticService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
