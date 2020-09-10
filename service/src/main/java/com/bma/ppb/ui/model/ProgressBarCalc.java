package com.bma.ppb.ui.model;

import com.bma.ppb.timeline.model.ReportingPeriodBean;

import java.util.Optional;

/**
 * Calculates project progress bar state.
 */
public final class ProgressBarCalc {

    private static final String PROJECT_PROGRESS_IS_EMPTY =
            "The following parameter should be specified: 'StartDate', 'CompleteDate' and 'Progress'.";
    private static final String PROJECT_IS_OUT_OF_QUARTER =
            "The project is out of scope of the current quarter.";
    private static final String GENERAL_ERROR =
            "Progress cannot be displayed.";

    private ProgressBarCalc() {}

    /**
     * Creates State Bean of the progress for the current project item.
     * @param project current project item
     * @param reportingPeriod current quarter and date
     * @return state bean
     */
    public static ProgressBarStateBean getProgressBarState(ProjectViewBean project, ReportingPeriodBean reportingPeriod)
            throws IllegalArgumentException {
        if (project == null || reportingPeriod == null) {
            throw new IllegalArgumentException(GENERAL_ERROR);
        }

        long startQuarterDay = reportingPeriod.getFirstMonth().atDay(1).toEpochDay();
        long endQuarterDay = reportingPeriod.getLastMonth().atEndOfMonth().toEpochDay();
        long quarterDurationDays = endQuarterDay - startQuarterDay;

        long projectStartDay = Optional.ofNullable(project.getStartDate())
                .orElseThrow(() -> new IllegalArgumentException(PROJECT_PROGRESS_IS_EMPTY))
                .toEpochDay();
        long projectCompleteDay = Optional.ofNullable(project.getCompleteDate())
                .orElseThrow(() -> new IllegalArgumentException(PROJECT_PROGRESS_IS_EMPTY))
                .toEpochDay();

        if (startQuarterDay > projectStartDay || endQuarterDay < projectCompleteDay) {
            throw new IllegalArgumentException(PROJECT_IS_OUT_OF_QUARTER);
        }

        var stateBuilder = ProgressBarStateBean.builder();
        // calc leftSpacePercent
        float t1 = projectStartDay - startQuarterDay;
        stateBuilder.leftSpacePercent(Math.round((t1 / quarterDurationDays) * 100));

        // calc progressBarSpacePercent
        float projectDurationDays = projectCompleteDay - projectStartDay;
        stateBuilder.progressBarSpacePercent(Math.round((projectDurationDays / quarterDurationDays) * 100));

        stateBuilder.color(deriveProgressBarColor(project));
        stateBuilder.progress(Optional.ofNullable(project.getProgressPercent()).orElse(0.0));

        return stateBuilder.build();
    }

    private static ProgressBarStateBean.ProgressBarColor deriveProgressBarColor(ProjectViewBean item) {
        if (item.getProgressState() == null) {
            return ProgressBarStateBean.ProgressBarColor.GREEN;
        }

        switch (item.getProgressState()) {
            case BEHIND_SCHEDULE:
                return ProgressBarStateBean.ProgressBarColor.YELLOW;
            case AT_RISK:
                return ProgressBarStateBean.ProgressBarColor.RED;
            default:
                return ProgressBarStateBean.ProgressBarColor.GREEN;
        }
    }


}
