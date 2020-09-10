package com.bma.ppb.ui.data;

import com.bma.ppb.project.model.ProjectBean;
import com.bma.ppb.project.service.ProjectService;
import com.bma.ppb.ui.model.ProjectViewBean;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@Slf4j
public class ProjectViewDataProvider extends AbstractBackEndDataProvider<ProjectViewBean, Void> {

    private final ProjectService projectService;

    @Autowired
    public ProjectViewDataProvider(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    protected Stream<ProjectViewBean> fetchFromBackEnd(Query<ProjectViewBean, Void> query) {
        log.info("Query projects, offset={}, limit={}", query.getOffset(), query.getLimit());
        return Stream.concat(
                Stream.of(ProjectViewBean.ofTimeline()),
                projectService.getProjectPage(Pageable.unpaged()).map(ProjectViewBean:: of).stream()
        );
    }

    @Override
    protected int sizeInBackEnd(Query<ProjectViewBean, Void> query) {
        return getItemsSizePlusTimeline(projectService.getProjectPage(Pageable.unpaged()));
    }

    private int getItemsSizePlusTimeline(Page<ProjectBean> projects) {
        return (int)projects.getTotalElements() + 1;
    }

    @Override
    public Object getId(ProjectViewBean item) {
        return item.getId();
    }
}
