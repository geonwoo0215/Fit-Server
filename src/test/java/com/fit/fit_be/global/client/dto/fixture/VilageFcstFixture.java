package com.fit.fit_be.global.client.dto.fixture;

import com.fit.fit_be.global.client.dto.VilageFcstResponse;
import com.fit.fit_be.global.client.dto.VilageFcstResponses;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VilageFcstFixture {

    private static final Integer NX = 55;
    private static final Integer NY = 127;
    private static final String DATA_TYPE = "JSON";
    private static final Integer PAGE_NO = 1;
    private static final Integer NUM_OF_ROWS = 12;
    private static final Integer TOTAL_COUNT = 4;

    public static VilageFcstResponse createVilageFcstResponse(String category, String fcstValue) {
        LocalDateTime now = LocalDateTime.now();

        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = now.format(DateTimeFormatter.ofPattern("HH00"));
        return VilageFcstResponse.builder()
                .baseDate(baseDate)
                .baseTime(baseTime)
                .category(category)
                .fcstDate(baseDate)
                .fcstTime(baseTime)
                .fcstValue(fcstValue)
                .nx(NX)
                .ny(NY)
                .build();
    }

    public static VilageFcstResponses createVilageFcstResponses(List<VilageFcstResponse> vilageFcstResponse) {
        return VilageFcstResponses.builder()
                .dataType(DATA_TYPE)
                .items(vilageFcstResponse)
                .pageNo(PAGE_NO)
                .numOfRows(NUM_OF_ROWS)
                .totalCount(TOTAL_COUNT)
                .build();
    }
}
