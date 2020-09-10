package com.bma.ppb.project.service;

import com.bma.ppb.project.model.ProjectBean;
import com.bma.ppb.project.repository.ProjectEntity;
import com.bma.ppb.project.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository repository;

    @Override
    public Page<ProjectBean> getProjectPage(Pageable pageRequest) {
        return repository.findAll(pageRequest).map(this::entityToBean);
    }

    @Override
    public void updateProject(ProjectBean project) {
        repository.save(beanToEntity(project));
    }

    @Override
    public ProjectBean createProject(ProjectBean project) {
        return entityToBean(repository.save(beanToEntity(project)));
    }

    private ProjectEntity beanToEntity(ProjectBean project) {
        ProjectEntity entity = new ProjectEntity();
        entity.setId(project.getId());
        entity.setName(project.getName());
        entity.setEndState(project.getEndState());
        entity.setStartDate(project.getStartDate());
        entity.setCompleteDate(project.getCompleteDate());
        entity.setProgressState(project.getProgressState());
        entity.setProgressPercent(project.getProgressPercent());
        return entity;
    }

    private ProjectBean entityToBean(ProjectEntity entity) {
        return ProjectBean.builder()
                .id(entity.getId())
                .name(entity.getName())
                .endState(entity.getEndState())
                .startDate(entity.getStartDate())
                .completeDate(entity.getCompleteDate())
                .progressState(entity.getProgressState())
                .progressPercent(entity.getProgressPercent())
                .build();
    }
}
