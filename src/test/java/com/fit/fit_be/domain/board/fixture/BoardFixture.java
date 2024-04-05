package com.fit.fit_be.domain.board.fixture;

import com.fit.fit_be.domain.board.dto.request.SaveBoardRequest;
import com.fit.fit_be.domain.board.dto.request.SearchBoardRequest;
import com.fit.fit_be.domain.board.dto.request.UpdateBoardRequest;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.model.Place;
import com.fit.fit_be.domain.board.model.RoadCondition;
import com.fit.fit_be.domain.board.model.Weather;
import com.fit.fit_be.domain.member.model.Member;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BoardFixture {

    private static final String CONTENT = "content";
    private static final Long LOWEST_TEMPERATURE = -7L;
    private static final Long HIGHEST_TEMPERATURE = 2L;
    private static final Boolean OPEN = true;

    public static Board createBoard(Member member) {
        return Board.builder()
                .member(member)
                .content(CONTENT)
                .lowestTemperature(LOWEST_TEMPERATURE)
                .highestTemperature(HIGHEST_TEMPERATURE)
                .open(OPEN)
                .weather(Weather.SUNNY)
                .roadCondition(RoadCondition.NORMAL)
                .place(Place.OUTING)
                .build();
    }

    public static List<Board> createBoards(Member member, int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> BoardFixture.createBoard(member))
                .collect(Collectors.toList());
    }


    public static SaveBoardRequest createSaveBoardRequest(List<String> imageUrls, Map<Long, Boolean> clothAppropriate) {
        return SaveBoardRequest.builder()
                .content(CONTENT)
                .lowestTemperature(LOWEST_TEMPERATURE)
                .highestTemperature(HIGHEST_TEMPERATURE)
                .open(OPEN)
                .weather(Weather.SUNNY)
                .roadCondition(RoadCondition.NORMAL)
                .place(Place.OUTING)
                .clothAppropriates(clothAppropriate)
                .imageUrls(imageUrls)
                .build();
    }

    public static UpdateBoardRequest createUpdateBoardRequest() {
        return UpdateBoardRequest.builder()
                .content(CONTENT)
                .lowestTemperature(LOWEST_TEMPERATURE)
                .highestTemperature(HIGHEST_TEMPERATURE)
                .open(OPEN)
                .weather(Weather.SUNNY)
                .roadCondition(RoadCondition.NORMAL)
                .place(Place.OUTING)
                .build();

    }

    public static SearchBoardRequest createSearchBoardRequest() {
        return SearchBoardRequest.builder()
                .lowestTemperature(LOWEST_TEMPERATURE)
                .highestTemperature(HIGHEST_TEMPERATURE)
                .weather(Weather.SUNNY)
                .roadCondition(RoadCondition.NORMAL)
                .place(Place.OUTING)
                .build();
    }

}
