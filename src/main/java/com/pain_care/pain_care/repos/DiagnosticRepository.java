package com.pain_care.pain_care.repos;

import com.pain_care.pain_care.domain.Diagnostic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiagnosticRepository extends JpaRepository<Diagnostic, Integer> {

    Diagnostic findFirstByUserId(Integer userId);

    void deleteByUserId(Integer userId);

}
