package com.bma.ppb.project.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RequestPredicates.accept;

@Configuration
public class ProjectApiRouteFunction extends RouterFunctions {

    private final ProjectApiHandler projectApiHandler;

    @Autowired
    public ProjectApiRouteFunction(ProjectApiHandler projectApiHandler) {
        this.projectApiHandler = projectApiHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> projectApiRoute() {
        return route().
                GET("/api/projects", accept(MediaType.APPLICATION_JSON), projectApiHandler::getProjectPage).
                build();
    }

}
