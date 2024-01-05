package com.pain_care.pain_care.service;

import com.pain_care.pain_care.domain.Diagnostic;
import com.pain_care.pain_care.model.DiagnosticDTO;
import com.pain_care.pain_care.repos.DiagnosticRepository;
import com.pain_care.pain_care.util.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DiagnosticService {

    private final DiagnosticRepository diagnosticRepository;

    public DiagnosticService(final DiagnosticRepository diagnosticRepository) {
        this.diagnosticRepository = diagnosticRepository;
    }

    public DiagnosticDTO getLatestDiagnostic(final Integer userId) {
        Diagnostic diagnostic = diagnosticRepository.findFirstByUserId(userId);
        if (diagnostic == null) {
            throw new NotFoundException("Diagnostic not found for user with ID: " + userId);
        }

        return mapToDTO(diagnostic, new DiagnosticDTO());
    }

    public Integer create(final DiagnosticDTO diagnosticDTO) {
        Diagnostic diagnostic = new Diagnostic();
        mapToEntity(diagnosticDTO, diagnostic);
        return diagnosticRepository.save(diagnostic).getId();
    }

    public void update(final Integer userId, final DiagnosticDTO diagnosticDTO) {
        Diagnostic diagnostic = diagnosticRepository.findFirstByUserId(userId);
        if (diagnostic == null) {
            throw new NotFoundException("Diagnostic not found for user with ID: " + userId);
        }

        mapToEntity(diagnosticDTO, diagnostic);
        diagnosticRepository.save(diagnostic);
    }

    public void delete(final Integer userId) {
        diagnosticRepository.deleteByUserId(userId);
    }

    private DiagnosticDTO mapToDTO(final Diagnostic diagnostic, final DiagnosticDTO diagnosticDTO) {
        diagnosticDTO.setId(diagnostic.getId());
        diagnosticDTO.setUserId(diagnostic.getUserId());
        // Map other fields
        return diagnosticDTO;
    }

    private Diagnostic mapToEntity(final DiagnosticDTO diagnosticDTO, final Diagnostic diagnostic) {
        diagnostic.setUserId(diagnosticDTO.getUserId());
        // Map other fields
        return diagnostic;
    }

    public float normalizeScore(float score) {
        // Ensure the score is between 0 and 10
        return Math.max(0, Math.min(score, 10));
    }
}