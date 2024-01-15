package com.fit.fit_be.domain.board.model;

import com.fit.fit_be.domain.board.dto.request.UpdateBoardRequest;
import com.fit.fit_be.domain.board.dto.response.BoardResponse;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.global.common.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String content;

    private Long lowestTemperature;

    private Long highestTemperature;

    private boolean open;

    private Weather weather;

    private RoadCondition roadCondition;

    @Builder
    public Board(Member member, String content, Long lowestTemperature, Long highestTemperature, boolean open, Weather weather, RoadCondition roadCondition) {
        this.member = member;
        this.content = content;
        this.lowestTemperature = lowestTemperature;
        this.highestTemperature = highestTemperature;
        this.open = open;
        this.weather = weather;
        this.roadCondition = roadCondition;
    }

    public BoardResponse toBoardResponse() {
        return BoardResponse.builder()
                .content(content)
                .lowestTemperature(lowestTemperature)
                .highestTemperature(highestTemperature)
                .open(open)
                .weather(weather)
                .roadCondition(roadCondition)
                .build();
    }

    public void updateBoard(UpdateBoardRequest updateBoardRequest) {
        this.content = Objects.requireNonNullElse(updateBoardRequest.getContent(), this.content);
        this.lowestTemperature = Objects.requireNonNullElse(updateBoardRequest.getLowestTemperature(), this.lowestTemperature);
        this.highestTemperature = Objects.requireNonNullElse(updateBoardRequest.getHighestTemperature(), this.highestTemperature);
        this.open = Objects.requireNonNullElse(updateBoardRequest.getOpen(), this.open);
        this.weather = Objects.requireNonNullElse(updateBoardRequest.getWeather(), this.weather);
        this.roadCondition = Objects.requireNonNullElse(updateBoardRequest.getRoadCondition(), this.roadCondition);
    }
}
