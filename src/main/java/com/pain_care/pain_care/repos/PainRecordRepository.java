package com.pain_care.pain_care.repos;

import com.pain_care.pain_care.domain.PainRecord;
import com.pain_care.pain_care.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PainRecordRepository extends JpaRepository<PainRecord, Integer> {

    PainRecord findFirstByUser(User user);

}
