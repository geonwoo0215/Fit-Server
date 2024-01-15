package com.fit.fit_be.domain.board.dto.request;

import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.model.RoadCondition;
import com.fit.fit_be.domain.board.model.Weather;
import com.fit.fit_be.domain.member.model.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SaveBoardRequest {

    private String content;

    private Long lowestTemperature;

    private Long highestTemperature;

    private boolean open;

    private Weather weather;

    private RoadCondition roadCondition;

    private List<String> imageUrls;


    public SaveBoardRequest(String content, Long lowestTemperature, Long highestTemperature, boolean open, Weather weather, RoadCondition roadCondition, List<String> imageUrls) {
        this.content = content;
        this.lowestTemperature = lowestTemperature;
        this.highestTemperature = highestTemperature;
        this.open = open;
        this.weather = weather;
        this.roadCondition = roadCondition;
        this.imageUrls = imageUrls;
    }

    public Board toBoard(Member member) {
        return Board.builder()
                .member(member)
                .content(content)
                .lowestTemperature(lowestTemperature)
                .highestTemperature(highestTemperature)
                .open(open)
                .weather(weather)
                .roadCondition(roadCondition)
                .build();
    }
}
