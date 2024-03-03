package com.fit.fit_be.domain.cloth.model;

import com.fit.fit_be.domain.cloth.dto.request.UpdateClothRequest;
import com.fit.fit_be.domain.cloth.dto.response.ClothResponse;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cloth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private ClothType type;

    private String information;

    private String size;

    @Builder
    public Cloth(Member member, ClothType type, String information, String size) {
        this.member = member;
        this.type = type;
        this.information = information;
        this.size = size;
    }

    public ClothResponse toClothResponse() {
        return ClothResponse.builder()
                .id(id)
                .type(type)
                .information(information)
                .size(size)
                .build();
    }

    public void updateCloth(UpdateClothRequest updateClothRequest) {
        this.type = Objects.requireNonNullElse(updateClothRequest.getType(), this.type);
        this.information = Objects.requireNonNullElse(updateClothRequest.getInformation(), this.information);
        this.size = Objects.requireNonNullElse(updateClothRequest.getSize(), this.size);
    }

}
