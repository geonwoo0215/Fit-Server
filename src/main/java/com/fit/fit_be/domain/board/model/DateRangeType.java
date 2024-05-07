package com.fit.fit_be.domain.board.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

public enum DateRangeType {
    DAILY("DAILY"),
    WEEKLY("WEEKLY");

    private final String type;

    DateRangeType(String type) {
        this.type = type;
    }

    public static DateRangeType of(String type) {
        return valueOf(type);
    }

    public List<String> getDates() {
        if (this == DAILY) {
            return List.of(LocalDate.now().minusDays(1).toString());
        } else if (this == WEEKLY) {
            return generateLastWeekDates();
        } else {
            throw new UnsupportedOperationException("Unsupported DateRangeType: " + this.type);
        }
    }

    private List<String> generateLastWeekDates() {
        LocalDate lastWeekSunday = LocalDate.now().with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
        LocalDate lastWeekMonday = lastWeekSunday.with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate date = lastWeekMonday; !date.isAfter(lastWeekSunday); date = date.plusDays(1)) {
            dates.add(date);
        }
        return getFormattedDates(dates);
    }

    private List<String> getFormattedDates(List<LocalDate> dates) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> formattedDates = new ArrayList<>();
        for (LocalDate date : dates) {
            formattedDates.add(date.format(formatter));
        }
        return formattedDates;
    }
}