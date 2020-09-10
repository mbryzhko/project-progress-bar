package com.bma.ppb.project.model;

import com.bma.ppb.project.repository.ProjectEndState;
import com.bma.ppb.project.repository.ProgressState;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * Public API bean of a project.
 */
@Data
@Builder
public class ProjectBean {

    private final Long id;
    private final String name;
    private final ProjectEndState endState;
    private final LocalDate startDate;
    private final LocalDate completeDate;
    private final ProgressState progressState;
    private final Double progressPercent;

}
