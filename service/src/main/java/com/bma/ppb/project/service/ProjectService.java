package com.bma.ppb.project.service;

import com.bma.ppb.project.model.ProjectBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectService {

    Page<ProjectBean> getProjectPage(Pageable pageRequest);

    void updateProject(ProjectBean project);

    ProjectBean createProject(ProjectBean project);

}
