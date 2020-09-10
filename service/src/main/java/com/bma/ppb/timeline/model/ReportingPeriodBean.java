package com.bma.ppb.timeline.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * Holds current reporting period (quarter) and current date.
 */
@Data
@Builder
public class ReportingPeriodBean {
    private final YearMonth firstMonth;
    private final LocalDate today;

    public YearMonth getLastMonth() {
        if (firstMonth != null) {
            return firstMonth.plusMonths(2);
        } else {
            return null;
        }
    }
}
