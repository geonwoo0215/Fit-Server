package com.fit.fit_be.domain.cloth.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Objects;

public enum ClothType {

    TOP("상의");

    private final String clothType;

    ClothType(String clothType) {
        this.clothType = clothType;
    }

    @JsonCreator
    public static ClothType of(String clothType) {
        return Arrays.stream(ClothType.values())
                .filter(ClothType -> Objects.equals(ClothType.getClothType(), clothType))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @JsonValue
    public String getClothType() {
        return clothType;
    }

}
