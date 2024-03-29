package com.fit.fit_be.domain.cloth.dto.request;

import com.fit.fit_be.domain.cloth.model.ClothType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateClothRequest {

    private ClothType type;

    private String information;

    private String size;

    @Builder
    public UpdateClothRequest(ClothType type, String information, String size) {
        this.type = type;
        this.information = information;
        this.size = size;
    }

}
