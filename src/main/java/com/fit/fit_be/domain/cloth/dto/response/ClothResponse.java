package com.fit.fit_be.domain.cloth.dto.response;

import com.fit.fit_be.domain.cloth.model.ClothType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ClothResponse {

    private Long id;

    private ClothType type;

    private String information;

    private String size;

    @Builder
    public ClothResponse(Long id, ClothType type, String information, String size) {
        this.id = id;
        this.type = type;
        this.information = information;
        this.size = size;
    }
}
