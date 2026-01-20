package com.expanse_tracker.utils;

import com.expanse_tracker.enums.DateRangeType;
import com.expanse_tracker.records.DateRange;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateRangeFactory {


    public static DateRange dateRange(DateRangeType type, LocalDate from, LocalDate to) {

        if (from != null && to != null) {
            return new DateRange(from.atStartOfDay(), to.plusDays(1).atStartOfDay());
        } else if (type != null) {
            return from(type);
        }

        return null;
    }






    public static DateRange from(DateRangeType type) {

        LocalDate today = LocalDate.now();

        return switch (type) {

            case THIS_WEEK -> new DateRange(
                    today.with(DayOfWeek.MONDAY).atStartOfDay(),
                    today.plusDays(1).atStartOfDay()
            );

            case THIS_MONTH -> new DateRange(
                    today.withDayOfMonth(1).atStartOfDay(),
                    today.plusDays(1).atStartOfDay()
            );

            case LAST_7_DAYS -> new DateRange(
                    today.minusWeeks(1).atStartOfDay(),
                    today.plusDays(1).atStartOfDay()
            );

            case LAST_30_DAYS -> new DateRange(
                    today.minusMonths(1).atStartOfDay(),
                    today.plusDays(1).atStartOfDay()
            );

            case LAST_90_DAYS -> new DateRange(
                    today.minusMonths(3).atStartOfDay(),
                    today.plusDays(1).atStartOfDay()
            );

            case LAST_MONTH -> {
                LocalDate firstDayOfLastMonth = today.minusMonths(1).withDayOfMonth(1);
                LocalDate firstDayOfThisMonth = today.withDayOfMonth(1);

                yield new DateRange(
                        firstDayOfLastMonth.atStartOfDay(),
                        firstDayOfThisMonth.atStartOfDay()
                );
            }

        };
    }


}
