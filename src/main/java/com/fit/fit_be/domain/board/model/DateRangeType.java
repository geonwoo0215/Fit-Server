package com.fit.fit_be.domain.board.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum DateRangeType {
    DAILY("DAILY", List.of(LocalDate.now().minusDays(1))),
    WEEKLY("WEEKLY", generateLastWeekDates());

    private final String type;
    private final List<LocalDate> dates;

    DateRangeType(String type, List<LocalDate> dates) {
        this.type = type;
        this.dates = dates;
    }

    public static DateRangeType of(String type) {
        return Arrays.stream(DateRangeType.values())
                .filter(DateRangeType -> Objects.equals(DateRangeType.getType(), type))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private static List<LocalDate> generateLastWeekDates() {
        LocalDate lastWeekSunday = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        LocalDate lastWeekMonday = lastWeekSunday.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate date = lastWeekMonday; !date.isAfter(lastWeekSunday); date = date.plusDays(1)) {
            dates.add(date);
        }
        return dates;
    }

    public String getType() {
        return type;
    }

    public List<LocalDate> getDates() {
        return dates;
    }
}
