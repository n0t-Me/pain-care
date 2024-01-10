package com.pain_care.pain_care.rest;

import com.pain_care.pain_care.model.PainRecordDTO;
import com.pain_care.pain_care.service.PainRecordService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/painRecords", produces = MediaType.APPLICATION_JSON_VALUE)
public class PainRecordResource {

    private final PainRecordService painRecordService;

    public PainRecordResource(final PainRecordService painRecordService) {
        this.painRecordService = painRecordService;
    }

    @GetMapping
    public ResponseEntity<List<PainRecordDTO>> getAllPainRecords() {
        return ResponseEntity.ok(painRecordService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PainRecordDTO> getPainRecord(
            @PathVariable(name = "id") final Integer id) {
        return ResponseEntity.ok(painRecordService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Integer> createPainRecord(
            @RequestBody @Valid final PainRecordDTO painRecordDTO) {
        final Integer createdId = painRecordService.create(painRecordDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Integer> updatePainRecord(@PathVariable(name = "id") final Integer id,
                                                    @RequestBody @Valid final PainRecordDTO painRecordDTO) {
        painRecordService.update(id, painRecordDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePainRecord(@PathVariable(name = "id") final Integer id) {
        painRecordService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
