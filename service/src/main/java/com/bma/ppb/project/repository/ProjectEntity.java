package com.bma.ppb.project.repository;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity(name = "project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "end_state")
    @Enumerated(EnumType.STRING)
    private ProjectEndState endState;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate completeDate;

    @Column(name = "progress_state")
    @Enumerated(EnumType.STRING)
    private ProgressState progressState;

    @Column(name = "progress_percent")
    private Double progressPercent;

}


