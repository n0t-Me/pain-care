package com.pain_care.pain_care.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class SymptomeListConverter implements AttributeConverter<List<Symptome>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<Symptome> symptomeList) {
        if (symptomeList == null || symptomeList.isEmpty()) {
            return null;
        }
        return symptomeList.stream()
                .map(Enum::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<Symptome> convertToEntityAttribute(String symptomeString) {
        if (symptomeString == null || symptomeString.trim().isEmpty()) {
            return null;
        }
        return Arrays.stream(symptomeString.split(DELIMITER))
                .map(Symptome::valueOf)
                .collect(Collectors.toList());
    }
}
