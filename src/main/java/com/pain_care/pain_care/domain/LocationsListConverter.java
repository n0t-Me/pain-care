package com.pain_care.pain_care.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class LocationsListConverter implements AttributeConverter<List<Locations>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(List<Locations> locationsList) {
        if (locationsList == null || locationsList.isEmpty()) {
            return null;
        }
        return locationsList.stream()
                .map(Enum::name)
                .collect(Collectors.joining(DELIMITER));
    }

    @Override
    public List<Locations> convertToEntityAttribute(String locationsString) {
        if (locationsString == null || locationsString.trim().isEmpty()) {
            return null;
        }
        return Arrays.stream(locationsString.split(DELIMITER))
                .map(Locations::valueOf)
                .collect(Collectors.toList());
    }
}
