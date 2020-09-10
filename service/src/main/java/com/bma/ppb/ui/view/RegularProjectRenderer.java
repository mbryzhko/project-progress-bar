package com.bma.ppb.ui.view;

import com.bma.ppb.timeline.model.ReportingPeriodBean;
import com.bma.ppb.ui.model.ProgressBarCalc;
import com.bma.ppb.ui.model.ProgressBarStateBean;
import com.bma.ppb.ui.model.ProjectViewBean;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

public class RegularProjectRenderer implements ProjectColumnRenderer {

    public static final RegularProjectRenderer INSTANCE = new RegularProjectRenderer();

    @Override
    public Component renderProgressBar(ProjectViewBean item, ReportingPeriodBean reportingPeriod) {
        HorizontalLayout progressBarContainer = new HorizontalLayout();
        progressBarContainer.setWidthFull();

        try {
            ProgressBarStateBean progressBarState = ProgressBarCalc.getProgressBarState(item, reportingPeriod);

            progressBarContainer.add(createLeftSpace(progressBarState));
            progressBarContainer.add(createProgressBar(progressBarState));
            progressBarContainer.add(createRightSpace(progressBarState));

        } catch (IllegalArgumentException e) {
            Label message = new Label(e.getMessage());
            progressBarContainer.add(message);
        }

        return progressBarContainer;
    }

    private Label createRightSpace(ProgressBarStateBean progressBarState) {
        Label rightSpace = new Label();
        rightSpace.setWidth(getHtmlPercent(progressBarState.getRightSpacePercent()));
        rightSpace.setClassName("progress-bar-end");
        return rightSpace;
    }

    private ProgressBar createProgressBar(ProgressBarStateBean progressBarState) {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setHeight("20px");
        progressBar.setMin(0);
        progressBar.setMax(100);
        progressBar.setWidth(getHtmlPercent(progressBarState.getProgressBarSpacePercent()));
        progressBar.setValue(progressBarState.getProgress());
        progressBar.setClassName(progressBarState.getColor().getCssClass());
        return progressBar;
    }

    private Label createLeftSpace(ProgressBarStateBean progressBarState) {
        Label leftSpace = new Label();
        leftSpace.setWidth(getHtmlPercent(progressBarState.getLeftSpacePercent()));
        leftSpace.setClassName("progress-bar-start");
        return leftSpace;
    }

    private String getHtmlPercent(float percentValue) {
        return String.format("%.2f%%", percentValue);
    }
}
