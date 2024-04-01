package com.fit.fit_be.domain.cloth.dto.request;

import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.model.ClothType;
import com.fit.fit_be.domain.member.model.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveClothRequest {

    private ClothType type;

    private String information;

    private String size;

    @Builder
    public SaveClothRequest(ClothType type, String information, String size) {
        this.type = type;
        this.information = information;
        this.size = size;
    }

    public Cloth toCloth(Member member) {
        return Cloth.builder()
                .member(member)
                .type(type)
                .information(information)
                .size(size)
                .build();
    }

}
