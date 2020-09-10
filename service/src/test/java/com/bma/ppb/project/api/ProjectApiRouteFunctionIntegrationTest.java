package com.bma.ppb.project.api;

import com.bma.ppb.project.model.ProjectBean;
import com.bma.ppb.project.model.ProjectBeanTestBuilder;
import com.bma.ppb.project.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest
@ActiveProfiles("test")
@Import({ProjectApiRouteFunction.class, ProjectApiHandler.class})
class ProjectApiRouteFunctionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    private static final int DEF_PAGE_SIZE = 20;
    private static final int FIRST_PAGE = 0;

    @Test
    public void getFirstPageOfProjects() throws Exception {
        List<ProjectBean> projects = List.of(ProjectBeanTestBuilder.simpleProjectBean("project foo"), ProjectBeanTestBuilder.simpleProjectBean("project bar"));
        Pageable pageable = PageRequest.of(FIRST_PAGE, DEF_PAGE_SIZE);

        Mockito
                .when(projectService.getProjectPage(Mockito.eq(pageable)))
                .thenReturn(new PageImpl<>(projects, pageable, 25));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/projects?page={page}&size={size}", FIRST_PAGE, DEF_PAGE_SIZE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.projects[0].name").value("project foo"))
                .andExpect(jsonPath("$.projects[1].name").value("project bar"))
                .andExpect(jsonPath("$.page").value(FIRST_PAGE))
                .andExpect(jsonPath("$.size").value(DEF_PAGE_SIZE))
                .andExpect(jsonPath("$.total").value(2));

    }

    @Test
    public void firstPageWithDefaultSizeIsReturnedByDefault() throws Exception {
        List<ProjectBean> projects = List.of(ProjectBeanTestBuilder.simpleProjectBean("project foo"), ProjectBeanTestBuilder.simpleProjectBean("project bar"));
        Pageable pageable = PageRequest.of(FIRST_PAGE, DEF_PAGE_SIZE);

        Mockito
                .when(projectService.getProjectPage(Mockito.eq(pageable)))
                .thenReturn(new PageImpl<>(projects, pageable, 25));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/projects", FIRST_PAGE, DEF_PAGE_SIZE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.page").value(FIRST_PAGE))
                .andExpect(jsonPath("$.size").value(DEF_PAGE_SIZE))
                .andExpect(jsonPath("$.total").value(2));

    }

    @Test
    public void emptyListWhenNoProjectsFound() throws Exception {
        Pageable pageable = PageRequest.of(FIRST_PAGE, DEF_PAGE_SIZE);

        Mockito
                .when(projectService.getProjectPage(Mockito.eq(pageable)))
                .thenReturn(new PageImpl<>(Collections.emptyList(), pageable, 0));

        mockMvc
                .perform(MockMvcRequestBuilders.get("/api/projects", FIRST_PAGE, DEF_PAGE_SIZE)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.projects").isEmpty())
                .andExpect(jsonPath("$.page").value(FIRST_PAGE))
                .andExpect(jsonPath("$.size").value(DEF_PAGE_SIZE))
                .andExpect(jsonPath("$.total").value(0));

    }


}