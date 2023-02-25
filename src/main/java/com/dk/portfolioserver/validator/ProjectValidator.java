package com.dk.portfolioserver.validator;

import com.dk.portfolioserver.dto.ProjectAddDto;
import com.dk.portfolioserver.model.Project;
import org.springframework.stereotype.Service;

@Service
public class ProjectValidator {

    private final int minNameLength = 3;

    public boolean isProjectValid(Project project) {
        return project != null
                && project.getId() != null
                && project.getName() != null
                && project.getName().length() >= minNameLength;
    }

    public boolean isProjectAddDtoValid(ProjectAddDto dto) {
        return dto != null
                && dto.getName() != null
                && dto.getName().length() >= minNameLength;
    }

}
