package com.fit.fit_be.domain.cloth.dto.response;

import com.fit.fit_be.domain.cloth.model.ClothType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ClothResponse {

    private ClothType type;

    private String information;

    private String size;

    private boolean shoe;

    @Builder
    public ClothResponse(ClothType type, String information, String size, boolean shoe) {
        this.type = type;
        this.information = information;
        this.size = size;
        this.shoe = shoe;
    }

}
