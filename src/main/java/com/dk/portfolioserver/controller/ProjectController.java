package com.dk.portfolioserver.controller;

import com.dk.portfolioserver.dto.ProjectAddDto;
import com.dk.portfolioserver.model.Project;
import com.dk.portfolioserver.service.ProjectService;
import com.dk.portfolioserver.validator.ProjectValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/project")
public class ProjectController {

    private final ProjectService projectService;

    private final ProjectValidator projectValidator;

    @GetMapping("{id}")
    public ResponseEntity<Project> getProject(@PathVariable("id") int id) {
        Optional<Project> optionalProject = projectService.getProject(id);
        if (optionalProject.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(optionalProject.get());
    }

    @GetMapping
    public List<Project> listProjects() {
        return projectService.listProjects();
    }

    @PostMapping
    public ResponseEntity<Project> getProject(@RequestBody ProjectAddDto dto) {
        if (!projectValidator.isProjectAddDtoValid(dto)) {
            return ResponseEntity.badRequest().build();
        }
        Project project = new Project();
        project.setName(dto.getName());
        return ResponseEntity.ok(projectService.saveProject(project));
    }

    @PutMapping
    public ResponseEntity<Project> updateProject(@RequestBody Project project) {
        if (!projectValidator.isProjectValid(project)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(projectService.saveProject(project));
    }

    @DeleteMapping("{id}")
    public void deleteProject(@PathVariable("id") int id) {
        projectService.deleteProject(id);
    }

}
