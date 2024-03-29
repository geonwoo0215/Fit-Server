package com.fit.fit_be.domain.board.dto.request;

import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.model.Place;
import com.fit.fit_be.domain.board.model.RoadCondition;
import com.fit.fit_be.domain.board.model.Weather;
import com.fit.fit_be.domain.member.model.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
public class SaveBoardRequest {

    private String content;

    private Long lowestTemperature;

    private Long highestTemperature;

    private Boolean open;

    private Weather weather;

    private RoadCondition roadCondition;

    private Place place;

    private Map<Long, Boolean> clothAppropriates;

    private List<String> imageUrls;

    @Builder
    public SaveBoardRequest(String content, Long lowestTemperature, Long highestTemperature, Boolean open, Weather weather, RoadCondition roadCondition, Place place, Map<Long, Boolean> clothAppropriates, List<String> imageUrls) {
        this.content = content;
        this.lowestTemperature = lowestTemperature;
        this.highestTemperature = highestTemperature;
        this.open = open;
        this.weather = weather;
        this.roadCondition = roadCondition;
        this.place = place;
        this.clothAppropriates = clothAppropriates;
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
                .place(place)
                .build();
    }
}
