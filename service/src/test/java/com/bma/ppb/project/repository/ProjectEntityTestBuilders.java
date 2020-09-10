package com.bma.ppb.project.repository;

import java.time.LocalDate;
import java.time.Month;

public class ProjectEntityTestBuilders {

    private static long SEQ = 0;

    public static ProjectEntity simplePersistedProjectEntity(String name) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(SEQ++);
        projectEntity.setEndState(ProjectEndState.GA);
        projectEntity.setName(name);
        projectEntity.setProgressState(ProgressState.ON_TRACK);
        projectEntity.setStartDate(LocalDate.of(2020, Month.JUNE, 1));
        projectEntity.setCompleteDate(LocalDate.of(2020, Month.JULY, 15));
        projectEntity.setProgressPercent(25.5d);
        return projectEntity;
    }

    public static ProjectEntity simpleNewProjectEntity(String name) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setEndState(ProjectEndState.GA);
        projectEntity.setName(name);
        projectEntity.setProgressState(ProgressState.ON_TRACK);
        projectEntity.setStartDate(LocalDate.of(2020, Month.JUNE, 1));
        projectEntity.setCompleteDate(LocalDate.of(2020, Month.JULY, 15));
        projectEntity.setProgressPercent(25.5d);
        return projectEntity;
    }

}