package com.fit.fit_be.domain.cloth.model;

import com.fit.fit_be.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cloth {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private ClothType type;

    private String information;

    private String size;

    private boolean shoe;

    @Builder
    public Cloth(Member member, ClothType type, String information, String size, boolean shoe) {
        this.member = member;
        this.type = type;
        this.information = information;
        this.size = size;
        this.shoe = shoe;
    }
}
