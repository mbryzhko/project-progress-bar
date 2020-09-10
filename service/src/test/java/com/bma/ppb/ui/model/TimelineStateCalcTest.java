package com.bma.ppb.ui.model;

import com.bma.ppb.timeline.model.ReportingPeriodBean;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class TimelineStateCalcTest {

    @Test
    public void eachMonthContainsYearMonth() {
        ReportingPeriodBean reportingPeriod = ReportingPeriodBean.builder()
                .firstMonth(YearMonth.of(2020, 8))
                .today(LocalDate.of(2020, 8, 1)).build();

        TimelineStateBean timelineState = TimelineStateCalc.getTimelineState(reportingPeriod);

        assertEquals(YearMonth.of(2020, 8), timelineState.getFirstMonth());
        assertEquals(YearMonth.of(2020, 9), timelineState.getSecondMonth());
        assertEquals(YearMonth.of(2020, 10), timelineState.getThirdMonth());
    }

    @Test
    public void quarterProgress() {
        ReportingPeriodBean reportingPeriod = ReportingPeriodBean.builder()
                .firstMonth(YearMonth.of(2020, 8))
                .today(LocalDate.of(2020, 9, 1)).build();

        TimelineStateBean timelineState = TimelineStateCalc.getTimelineState(reportingPeriod);

        assertEquals(34.0, timelineState.getQuarterProgressPercent());
    }

    @Test
    public void errorCalculatingProgress() {
        ReportingPeriodBean reportingPeriod = ReportingPeriodBean.builder()
                .firstMonth(YearMonth.of(2020, 8))
                .today(LocalDate.of(2020, 7, 1)).build();

        TimelineStateBean timelineState = TimelineStateCalc.getTimelineState(reportingPeriod);

        assertEquals(0, timelineState.getQuarterProgressPercent());

    }

}