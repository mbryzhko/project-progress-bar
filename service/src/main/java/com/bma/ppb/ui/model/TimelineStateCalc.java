package com.bma.ppb.ui.model;

import com.bma.ppb.timeline.model.ReportingPeriodBean;
import com.bma.ppb.ui.view.TimelineRenderer;

import java.time.YearMonth;

/**
 * Calculates state of timeline view.
 *
 * @see TimelineRenderer
 */
public final class TimelineStateCalc {

    private TimelineStateCalc() {}

    public static TimelineStateBean getTimelineState(ReportingPeriodBean reportingPeriod) {

        YearMonth firstMonthOfQuarter = reportingPeriod.getFirstMonth();
        YearMonth thirdMonthOfQuarter = reportingPeriod.getLastMonth();

        var builder = TimelineStateBean.builder()
                .firstMonth(firstMonthOfQuarter)
                .secondMonth(firstMonthOfQuarter.plusMonths(1))
                .thirdMonth(thirdMonthOfQuarter)
                .quarterProgressPercent(calculateQuarterProgress(reportingPeriod));

        return builder.build();
    }

    private static double calculateQuarterProgress(ReportingPeriodBean reportingPeriod) {
        long startQuarterDay = reportingPeriod.getFirstMonth().atDay(1).toEpochDay();
        long endQuarterDay = reportingPeriod.getLastMonth().atEndOfMonth().toEpochDay();
        long quarterDurationDays = endQuarterDay - startQuarterDay;
        long todayDay = reportingPeriod.getToday().toEpochDay();

        if (todayDay < startQuarterDay || todayDay > endQuarterDay) {
            // Today is our of quarter boundaries
            return 0;
        }

        return Math.round(((double)(todayDay - startQuarterDay) / quarterDurationDays) * 100);
    }

}
