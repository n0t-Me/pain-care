package com.pain_care.pain_care.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class FeelingsListConverter implements AttributeConverter<List<Feelings>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<Feelings> feelingsList) {
        if (feelingsList == null || feelingsList.isEmpty()) {
            return null;
        }
        return feelingsList.stream()
                .map(Enum::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<Feelings> convertToEntityAttribute(String feelingsString) {
        if (feelingsString == null || feelingsString.trim().isEmpty()) {
            return null;
        }
        return Arrays.stream(feelingsString.split(DELIMITER))
                .map(Feelings::valueOf)
                .collect(Collectors.toList());
    }
}
