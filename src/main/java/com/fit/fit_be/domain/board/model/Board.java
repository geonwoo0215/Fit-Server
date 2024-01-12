package com.fit.fit_be.domain.board.model;

import com.fit.fit_be.domain.member.model.Member;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String content;

    private String lowestTemperature;

    private String highestTemperature;

    private boolean open;

    private Weather weather;

    private RoadCondition roadCondition;

}
