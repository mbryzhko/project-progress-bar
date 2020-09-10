package com.bma.ppb.ui.model;

import com.bma.ppb.project.model.ProjectBean;
import com.bma.ppb.project.repository.ProgressState;
import com.bma.ppb.project.repository.ProjectEndState;
import com.bma.ppb.ui.view.ProjectColumnRenderer;
import com.bma.ppb.ui.view.RegularProjectRenderer;
import com.bma.ppb.ui.view.TimelineRenderer;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;

/**
 * View presentation of a project.
 * @see ProjectBean
 */
@Data
@Builder
public class ProjectViewBean {

    // Reserved identified for timeline row
    private static final Long TIMELINE_ID = -1L;

    private final Long  id;
    @Setter private String name;
    @Setter private ProjectEndState endState;
    @Setter private LocalDate startDate;
    @Setter private LocalDate completeDate;
    @Setter private ProgressState progressState;
    @Setter private Double progressPercent;
    private final Kind kind;
    private final ProjectColumnRenderer renderer;

    public static ProjectViewBean of(ProjectBean projectBean) {
        return ProjectViewBean.builder()
                .id(projectBean.getId())
                .name(projectBean.getName())
                .endState(projectBean.getEndState())
                .startDate(projectBean.getStartDate())
                .completeDate(projectBean.getCompleteDate())
                .progressState(projectBean.getProgressState())
                .progressPercent(projectBean.getProgressPercent())
                .kind(Kind.PROJECT)
                .renderer(RegularProjectRenderer.INSTANCE)
                .build();
    }

    public static ProjectViewBean ofTimeline() {
        return ProjectViewBean.builder()
                .id(TIMELINE_ID)
                .kind(Kind.TIMELINE)
                .renderer(TimelineRenderer.INSTANCE)
                .build();
    }

    public enum Kind {
        PROJECT,
        TIMELINE
    }
}
