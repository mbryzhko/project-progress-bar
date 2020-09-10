package com.bma.ppb.ui.view;

import com.bma.ppb.timeline.model.ReportingPeriodBean;
import com.bma.ppb.ui.model.ProjectViewBean;
import com.bma.ppb.ui.model.TimelineStateBean;
import com.bma.ppb.ui.model.TimelineStateCalc;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

import java.time.format.DateTimeFormatter;

/**
 * Renders timeline on top of project list view.
 *
 * @see ProjectViewBean#ofTimeline()
 */
public class TimelineRenderer implements ProjectColumnRenderer {

    public static TimelineRenderer INSTANCE = new TimelineRenderer();

    private static final DateTimeFormatter QUARTER_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMM uu");

    @Override
    public Component renderProgressBar(ProjectViewBean item, ReportingPeriodBean reportingPeriod) {
        VerticalLayout layout = new VerticalLayout();
        layout.setPadding(false);

        TimelineStateBean state = TimelineStateCalc.getTimelineState(reportingPeriod);

        HorizontalLayout quarterDatesContainer = new HorizontalLayout();
        quarterDatesContainer.setWidthFull();

        Label m1Label = new Label(state.getFirstMonth().format(QUARTER_DATE_FORMATTER));
        m1Label.setClassName("timeline-m1");
        Label m2Label = new Label(state.getSecondMonth().format(QUARTER_DATE_FORMATTER));
        m2Label.setClassName("timeline-m2");
        Label m3Label = new Label(state.getThirdMonth().format(QUARTER_DATE_FORMATTER));
        m3Label.setClassName("timeline-m3");
        quarterDatesContainer.add(m1Label, m2Label, m3Label);

        ProgressBar progressBar = new ProgressBar();
        progressBar.setMin(0);
        progressBar.setMax(100);
        progressBar.setValue(state.getQuarterProgressPercent());
        progressBar.setHeight("4px");

        Label leftLabel = new Label();
        leftLabel.setClassName("progress-bar-start");
        Label rightLabel = new Label();
        rightLabel.setClassName("progress-bar-end");

        HorizontalLayout progressBarContainer = new HorizontalLayout(leftLabel, progressBar, rightLabel);
        progressBarContainer.setWidthFull();

        layout.add(quarterDatesContainer);
        layout.add(progressBarContainer);



        return layout;
    }
}
