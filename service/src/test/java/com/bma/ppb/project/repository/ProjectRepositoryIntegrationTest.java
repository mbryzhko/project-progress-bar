package com.bma.ppb.project.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DataJpaTest
class ProjectRepositoryIntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    @BeforeEach
    public void cleanup() {
        projectRepository.findAll().forEach(projectRepository::delete);
    }

    @Test
    public void saveAndLoad() {
        Assertions.assertNotNull(projectRepository);

        var newEntity = ProjectEntityTestBuilders.simpleNewProjectEntity("simple project");
        newEntity = projectRepository.save(newEntity);

        assertTrue(newEntity.getId() > 0);

        ProjectEntity loadedEntity = projectRepository.findById(newEntity.getId()).orElseThrow();
        assertEquals(newEntity, loadedEntity);
    }

}