package com.pain_care.pain_care.service;

import com.pain_care.pain_care.domain.PainRecord;
import com.pain_care.pain_care.domain.User;
import com.pain_care.pain_care.model.PainRecordDTO;
import com.pain_care.pain_care.repos.PainRecordRepository;
import com.pain_care.pain_care.repos.UserRepository;
import com.pain_care.pain_care.util.NotFoundException;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PainRecordService {

    private final PainRecordRepository painRecordRepository;
    private final UserRepository userRepository;

    public PainRecordService(final PainRecordRepository painRecordRepository,
                             final UserRepository userRepository) {
        this.painRecordRepository = painRecordRepository;
        this.userRepository = userRepository;
    }

    public List<PainRecordDTO> findAll() {
        final List<PainRecord> painRecords = painRecordRepository.findAll(Sort.by("id"));
        return painRecords.stream()
                .map(painRecord -> mapToDTO(painRecord, new PainRecordDTO()))
                .toList();
    }

    public PainRecordDTO get(final Integer id) {
        return painRecordRepository.findById(id)
                .map(painRecord -> mapToDTO(painRecord, new PainRecordDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final PainRecordDTO painRecordDTO) {
        final PainRecord painRecord = new PainRecord();
        mapToEntity(painRecordDTO, painRecord);
        return painRecordRepository.save(painRecord).getId();
    }

    public void update(final Integer id, final PainRecordDTO painRecordDTO) {
        final PainRecord painRecord = painRecordRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(painRecordDTO, painRecord);
        painRecordRepository.save(painRecord);
    }

    public void delete(final Integer id) {
        painRecordRepository.deleteById(id);
    }

    private PainRecordDTO mapToDTO(final PainRecord painRecord, final PainRecordDTO painRecordDTO) {
        painRecordDTO.setId(painRecord.getId());
        painRecordDTO.setLevel(painRecord.getLevel());
        painRecordDTO.setLocations(painRecord.getLocations());
        painRecordDTO.setSymptoms(painRecord.getSymptoms());
        painRecordDTO.setMakePainWorse(painRecord.getMakePainWorse());
        painRecordDTO.setFeelings(painRecord.getFeelings());
        painRecordDTO.setUser(painRecord.getUser() == null ? null : painRecord.getUser().getId());
        painRecordDTO.setDateCreated(painRecord.getDateCreated());
        return painRecordDTO;
    }

    private PainRecord mapToEntity(final PainRecordDTO painRecordDTO, final PainRecord painRecord) {
        painRecord.setLevel(painRecordDTO.getLevel());
        painRecord.setLocations(painRecordDTO.getLocations());
        painRecord.setSymptoms(painRecordDTO.getSymptoms());
        painRecord.setMakePainWorse(painRecordDTO.getMakePainWorse());
        painRecord.setFeelings(painRecordDTO.getFeelings());
        final User user = painRecordDTO.getUser() == null ? null : userRepository.findById(painRecordDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        painRecord.setUser(user);
        return painRecord;
    }

}
