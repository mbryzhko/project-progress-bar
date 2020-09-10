package com.bma.ppb.ui.data;

import com.bma.ppb.project.model.ProjectBean;
import com.bma.ppb.project.model.ProjectBeanTestBuilder;
import com.bma.ppb.project.service.ProjectService;
import com.bma.ppb.ui.model.ProjectViewBean;
import com.vaadin.flow.data.provider.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProjectViewDataProviderTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectViewDataProvider dataProvider;

    @Test
    public void countIsSizeOfAllElementsPlusTimeline() {
        List<ProjectBean> projects = List.of(ProjectBeanTestBuilder.simpleProjectBean("project 1"), ProjectBeanTestBuilder.simpleProjectBean("project 2"));
        Mockito.when(projectService.getProjectPage(Mockito.isA(Pageable.class))).thenReturn(new PageImpl<>(projects));

        int actualSize = dataProvider.size(new Query<>());

        assertEquals(projects.size() + 1, actualSize);

        var pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(projectService, Mockito.times(1)).getProjectPage(pageableCaptor.capture());
        assertTrue(pageableCaptor.getValue().isUnpaged(), "Fetching all projects");
    }

    @Test
    public void timelineItemIsAddedToListOfProjects() {
        List<ProjectBean> projects = List.of(ProjectBeanTestBuilder.simpleProjectBean("project 1"), ProjectBeanTestBuilder.simpleProjectBean("project 2"));
        Mockito.when(projectService.getProjectPage(Mockito.isA(Pageable.class))).thenReturn(new PageImpl<>(projects));

        List<ProjectViewBean> actualProjects = dataProvider.fetchFromBackEnd(new Query<>()).collect(Collectors.toList());

        assertEquals(projects.size() + 1, actualProjects.size());
        assertEquals(ProjectViewBean.Kind.TIMELINE, actualProjects.get(0).getKind());
        assertEquals(ProjectViewBean.Kind.PROJECT, actualProjects.get(1).getKind());
        assertEquals(ProjectViewBean.Kind.PROJECT, actualProjects.get(2).getKind());

        var pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        Mockito.verify(projectService, Mockito.times(1)).getProjectPage(pageableCaptor.capture());
        assertTrue(pageableCaptor.getValue().isUnpaged(), "Fetching all projects");
    }

}