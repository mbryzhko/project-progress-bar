package com.bma.ppb.project.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectPageApiResponse {

    private final List<ProjectBean> projects;
    private final int page;
    private final int size;
    private final int total;

}
