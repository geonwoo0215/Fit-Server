package com.fit.fit_be.domain.board.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum DateRangeType {
    DAILY("DAILY", List.of(LocalDate.now().minusDays(1).toString())),
    WEEKLY("WEEKLY", generateLastWeekDates());

    private final String type;
    private final List<String> dates;

    DateRangeType(String type, List<String> dates) {
        this.type = type;
        this.dates = dates;
    }

    public static DateRangeType of(String type) {
        return Arrays.stream(DateRangeType.values())
                .filter(dateRangeType -> Objects.equals(dateRangeType.getType(), type))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    private static List<String> generateLastWeekDates() {
        LocalDate lastWeekSunday = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        LocalDate lastWeekMonday = lastWeekSunday.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate date = lastWeekMonday; !date.isAfter(lastWeekSunday); date = date.plusDays(1)) {
            dates.add(date);
        }
        return getFormattedDates(dates);
    }

    public static List<String> getFormattedDates(List<LocalDate> dates) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> formattedDates = new ArrayList<>();
        for (LocalDate date : dates) {
            formattedDates.add(date.format(formatter));
        }
        return formattedDates;
    }

    public String getType() {
        return type;
    }

    public List<String> getDates() {
        return dates;
    }
}