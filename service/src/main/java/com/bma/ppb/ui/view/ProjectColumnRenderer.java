package com.bma.ppb.ui.view;

import com.bma.ppb.timeline.model.ReportingPeriodBean;
import com.bma.ppb.ui.model.ProjectViewBean;
import com.vaadin.flow.component.Component;

/**
 * Renders column view components.
 */
public interface ProjectColumnRenderer {

    /**
     * Creates Vaadin Component that contains UI of the column.
     * @param item current project item
     * @param reportingPeriod current reporting period
     * @return Vaadin Component
     */
    Component renderProgressBar(ProjectViewBean item, ReportingPeriodBean reportingPeriod);

}
