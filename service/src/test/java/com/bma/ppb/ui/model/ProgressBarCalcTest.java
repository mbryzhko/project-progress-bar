package com.bma.ppb.ui.model;

import com.bma.ppb.timeline.model.ReportingPeriodBean;
import com.bma.ppb.project.repository.ProgressState;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ProgressBarCalcTest {

    @Test
    public void projectInTheMiddleOfQuarter() {
        ReportingPeriodBean reportingPeriod = getReportingPeriod();

        ProjectViewBean project = ProjectViewBean.builder()
                .startDate(LocalDate.of(2020, 9, 1))
                .completeDate(LocalDate.of(2020, 9, 30)).build();

        ProgressBarStateBean state = ProgressBarCalc.getProgressBarState(project, reportingPeriod);

        assertEquals(34.0, state.getLeftSpacePercent());
        assertEquals(32.0, state.getProgressBarSpacePercent());
        assertEquals(34.0, state.getRightSpacePercent());
    }

    @Test
    public void throwErrorWhenValuesAreEmpty() {
        ReportingPeriodBean reportingPeriod = getReportingPeriod();

        ProjectViewBean projectWithEmptyProgress = ProjectViewBean.builder()
                .startDate(null)
                .completeDate(null).build();

        assertThrows(IllegalArgumentException.class, () -> {
            ProgressBarCalc.getProgressBarState(projectWithEmptyProgress, reportingPeriod);
        });
    }

    @Test
    public void throwErrorWhenProjectIsOutOfCurrentQuarter() {
        ReportingPeriodBean reportingPeriod = getReportingPeriod();

        ProjectViewBean projectWithEmptyProgress = ProjectViewBean.builder()
                .startDate(LocalDate.of(2020, 5, 1))
                .completeDate(LocalDate.of(2020, 5, 30))
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            ProgressBarCalc.getProgressBarState(projectWithEmptyProgress, reportingPeriod);
        });
    }

    @Test
    public void greenProgressBarWhenProgressOnTrack() {
        ReportingPeriodBean reportingPeriod = getReportingPeriod();

        ProjectViewBean project = ProjectViewBean.builder()
                .startDate(LocalDate.of(2020, 9, 1))
                .completeDate(LocalDate.of(2020, 9, 30))
                .progressState(ProgressState.ON_TRACK).build();

        var progressBarColor = ProgressBarCalc.getProgressBarState(project, reportingPeriod).getColor();

        assertEquals(ProgressBarStateBean.ProgressBarColor.GREEN, progressBarColor);

    }

    @Test
    public void nullValueOfProgressFallbackToZero(){
        ReportingPeriodBean reportingPeriod = getReportingPeriod();

        ProjectViewBean project = ProjectViewBean.builder()
                .startDate(LocalDate.of(2020, 9, 1))
                .completeDate(LocalDate.of(2020, 9, 30))
                .progressState(ProgressState.ON_TRACK)
                .progressPercent(null).build();

        ProgressBarStateBean state = ProgressBarCalc.getProgressBarState(project, reportingPeriod);

        assertEquals(0, state.getProgress());
    }


    private ReportingPeriodBean getReportingPeriod() {
        return ReportingPeriodBean.builder()
                .firstMonth(YearMonth.of(2020, 8))
                .today(LocalDate.of(2020, 8, 1)).build();
    }
}