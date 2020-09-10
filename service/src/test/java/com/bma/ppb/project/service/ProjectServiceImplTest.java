package com.bma.ppb.project.service;

import com.bma.ppb.project.model.ProjectBeanTestBuilder;
import com.bma.ppb.project.repository.ProjectEntity;
import com.bma.ppb.project.model.ProjectBean;
import com.bma.ppb.project.repository.ProgressState;
import com.bma.ppb.project.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static com.bma.ppb.project.repository.ProjectEndState.GA;
import static com.bma.ppb.project.repository.ProjectEntityTestBuilders.simplePersistedProjectEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Mock
    private ProjectRepository projectRepository;

    private static final int FIRST_PAGE = 0;
    private static final int PAGE_SIZE = 2;

    private static final String PROJECT_1_NAME = "pr 1";
    private static final String PROJECT_2_NAME = "pr 2";

    @Test
    public void getListOfProjectForGivenPage() {
        PageRequest pageRequest = PageRequest.of(FIRST_PAGE, PAGE_SIZE);

        PageImpl<ProjectEntity> projectEntityPage = new PageImpl<>(
                List.of(simplePersistedProjectEntity(PROJECT_1_NAME), simplePersistedProjectEntity(PROJECT_2_NAME)),
                pageRequest,
                3
        );

        ProjectEntity firstProjectEntity = projectEntityPage.getContent().get(0);

        when(projectRepository.findAll(eq(pageRequest))).thenReturn(projectEntityPage);

        Page<ProjectBean> actualProjectBeans = projectService.getProjectPage(pageRequest);

        ProjectBean firstProjectBean = actualProjectBeans.getContent().get(0);
        assertEquals(firstProjectEntity.getId(), firstProjectBean.getId());
        assertEquals(PROJECT_1_NAME, firstProjectBean.getName());
        assertEquals(GA, firstProjectBean.getEndState());
        assertEquals(LocalDate.of(2020, Month.JUNE, 1), firstProjectBean.getStartDate());
        assertEquals(LocalDate.of(2020, Month.JULY, 15), firstProjectBean.getCompleteDate());
        assertEquals(ProgressState.ON_TRACK, firstProjectBean.getProgressState());
        assertEquals(25.5f, firstProjectBean.getProgressPercent());
    }

    @Test
    public void projectCanBeUpdated() {
        ProjectBean project = ProjectBeanTestBuilder.simpleProjectBean("project new name");
        ProjectEntity savedProjectEntity = simplePersistedProjectEntity("project new name");

        Mockito.when(projectRepository.save(Mockito.isA(ProjectEntity.class))).thenReturn(savedProjectEntity);

        projectService.updateProject(project);

        ArgumentCaptor<ProjectEntity> captor = ArgumentCaptor.forClass(ProjectEntity.class);
        Mockito.verify(projectRepository, times(1)).save(captor.capture());
        ProjectEntity actualEntity = captor.getValue();
        assertEquals(project.getId(), actualEntity.getId());
        assertEquals(project.getName(), actualEntity.getName());
        assertEquals(project.getEndState(), actualEntity.getEndState());
        assertEquals(LocalDate.of(2020, Month.JUNE, 1), actualEntity.getStartDate());
        assertEquals(LocalDate.of(2020, Month.JULY, 15), actualEntity.getCompleteDate());
        assertEquals(ProgressState.ON_TRACK, actualEntity.getProgressState());
        assertEquals(25.5f, actualEntity.getProgressPercent());
    }

    @Test
    public void newProjectCanBeCreated() {
        ProjectBean transientProject = ProjectBeanTestBuilder.newSimpleProjectBean("project new name");

        ProjectEntity savedProjectEntity = simplePersistedProjectEntity("project new name");

        Mockito.when(projectRepository.save(Mockito.isA(ProjectEntity.class))).thenReturn(savedProjectEntity);

        ProjectBean savedProject = projectService.createProject(transientProject);

        assertEquals(transientProject.getName(), savedProject.getName());
        assertEquals(transientProject.getEndState(), savedProject.getEndState());
        assertTrue(savedProject.getId() > 0);
    }


}