package com.pain_care.pain_care.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class MakePainWorseListConverter implements AttributeConverter<List<MakePainWorse>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<MakePainWorse> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        return attribute.stream()
                .map(MakePainWorse::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<MakePainWorse> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        return Arrays.stream(dbData.split(DELIMITER))
                .map(MakePainWorse::valueOf)
                .collect(Collectors.toList());
    }
}
