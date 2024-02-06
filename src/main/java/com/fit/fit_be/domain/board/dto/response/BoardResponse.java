package com.fit.fit_be.domain.board.dto.response;

import com.fit.fit_be.domain.board.model.RoadCondition;
import com.fit.fit_be.domain.board.model.Weather;
import com.fit.fit_be.domain.cloth.dto.response.ClothResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardResponse {

    private Long id;

    private String content;

    private Long lowestTemperature;

    private Long highestTemperature;

    private boolean open;

    private Weather weather;

    private RoadCondition roadCondition;

    private List<ClothResponse> clothResponses;

    private List<String> imageUrls;

    private boolean like;

    private String nickname;

    private boolean isMine;

    @Builder
    public BoardResponse(Long id, String content, Long lowestTemperature, Long highestTemperature, boolean open, Weather weather, RoadCondition roadCondition, List<ClothResponse> clothResponses, List<String> imageUrls, boolean like, String nickname, boolean isMine) {
        this.id = id;
        this.content = content;
        this.lowestTemperature = lowestTemperature;
        this.highestTemperature = highestTemperature;
        this.open = open;
        this.weather = weather;
        this.roadCondition = roadCondition;
        this.clothResponses = clothResponses;
        this.imageUrls = imageUrls;
        this.like = like;
        this.nickname = nickname;
        this.isMine = isMine;
    }
}
