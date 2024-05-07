package com.fit.fit_be.domain.weather;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum ForecastTime {

    TWO_AM("0200"),
    FIVE_AM("0500"),
    EIGHT_AM("0800"),
    ELEVEN_AM("1100"),
    TWO_PM("1400"),
    FIVE_PM("1700"),
    EIGHT_PM("2000"),
    ELEVEN_PM("2300");

    private final String baseTime;

    ForecastTime(String baseTime) {
        this.baseTime = baseTime;
    }

    public static ForecastTime of() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();

        if (minute >= 10) {
            hour++;
            if (hour == 24) {
                hour = 0;
            }
        }

        if (hour < 2 || (hour == 2 && minute < 10)) {
            return ELEVEN_PM;
        } else if (hour < 5 || (hour == 5 && minute < 10)) {
            return TWO_AM;
        } else if (hour < 8 || (hour == 8 && minute < 10)) {
            return FIVE_AM;
        } else if (hour < 11 || (hour == 11 && minute < 10)) {
            return EIGHT_AM;
        } else if (hour < 14 || (hour == 14 && minute < 10)) {
            return ELEVEN_AM;
        } else if (hour < 17 || (hour == 17 && minute < 10)) {
            return TWO_PM;
        } else if (hour < 20 || (hour == 20 && minute < 10)) {
            return FIVE_PM;
        } else {
            return EIGHT_PM;
        }
    }

    public String getBaseTime() {
        return baseTime;
    }

    public String getBaseDate() {
        return calculateBaseDate();
    }

    private String calculateBaseDate() {
        LocalDateTime now = LocalDateTime.now();
        if (now.getHour() < 2) {
            now = now.minusDays(1);
        }
        return now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}