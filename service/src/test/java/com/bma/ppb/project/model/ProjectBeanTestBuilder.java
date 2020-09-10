package com.bma.ppb.project.model;

import com.bma.ppb.project.repository.ProgressState;
import com.bma.ppb.project.repository.ProjectEndState;

import java.time.LocalDate;
import java.time.Month;

public class ProjectBeanTestBuilder {

    private static long SEQ = 0;

    public static ProjectBean simpleProjectBean(String name) {
        return ProjectBean.builder()
                .id(SEQ++)
                .name(name)
                .endState(ProjectEndState.GA)
                .startDate(LocalDate.of(2020, Month.JUNE, 1))
                .completeDate(LocalDate.of(2020, Month.JULY, 15))
                .progressState(ProgressState.ON_TRACK)
                .progressPercent(25.5d)
                .build();
    }

    public static ProjectBean newSimpleProjectBean(String name) {
        return ProjectBean.builder()
                .id(null)
                .name(name)
                .endState(ProjectEndState.GA)
                .build();
    }

}