package com.fit.fit_be.domain.board.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Objects;

public enum DateRangeType {
    DAILY("DAILY", LocalDateTime.now().minusDays(1).with(LocalTime.of(0, 0)), LocalDateTime.now().with(LocalTime.of(0, 0))),
    WEEKLY("WEEKLY", LocalDateTime.now().minusWeeks(1).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.of(0, 0)), LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.of(0, 0)));

    private final String type;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    DateRangeType(String type, LocalDateTime startDate, LocalDateTime endDate) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static DateRangeType of(String type) {
        return Arrays.stream(DateRangeType.values())
                .filter(DateRangeType -> Objects.equals(DateRangeType.getType(), type))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
