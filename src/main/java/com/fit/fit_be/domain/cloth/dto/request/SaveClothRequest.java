package com.fit.fit_be.domain.cloth.dto.request;

import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.model.ClothType;
import com.fit.fit_be.domain.member.model.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveClothRequest {

    private ClothType type;

    private String information;

    private String size;

    private Boolean shoe;

    public SaveClothRequest(ClothType type, String information, String size, Boolean shoe) {
        this.type = type;
        this.information = information;
        this.size = size;
        this.shoe = shoe;
    }

    public Cloth toCloth(Member member) {
        return Cloth.builder()
                .member(member)
                .information(information)
                .size(size)
                .shoe(shoe)
                .build();
    }

}
