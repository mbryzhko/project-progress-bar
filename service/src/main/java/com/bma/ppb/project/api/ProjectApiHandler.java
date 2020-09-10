package com.bma.ppb.project.api;

import com.bma.ppb.project.model.ProjectBean;
import com.bma.ppb.project.model.ProjectPageApiRequest;
import com.bma.ppb.project.service.ProjectService;
import com.bma.ppb.project.model.ProjectPageApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.ServletException;
import java.io.IOException;

@Component
@AllArgsConstructor
public class ProjectApiHandler {

    private final ProjectService projectService;

    private final static int DEF_PAGE_NUM = 0;
    private final static int DEF_PAGE_SIZE = 20;

    public ServerResponse getProjectPage(ServerRequest request) throws ServletException, IOException {
        ProjectPageApiRequest pageApiRequest = new ProjectPageApiRequest(
                request.param("page").map(Integer::parseInt).orElse(DEF_PAGE_NUM),
                request.param("size").map(Integer::parseInt).orElse(DEF_PAGE_SIZE));
        Page<ProjectBean> projectPage = projectService.getProjectPage(pageApiRequest.getPageable());
        ProjectPageApiResponse apiResponse = ProjectPageApiResponse.builder()
                .projects(projectPage.getContent())
                .page(projectPage.getNumber())
                .size(projectPage.getSize())
                .total(projectPage.getTotalPages())
                .build();
        return ServerResponse.ok().body(apiResponse);
    }

}
