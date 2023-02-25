package com.dk.portfolioserver.controller;

import com.dk.portfolioserver.dto.ProjectAddDto;
import com.dk.portfolioserver.model.Project;
import com.dk.portfolioserver.service.ProjectService;
import com.dk.portfolioserver.validator.ProjectValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@SpringJUnitConfig
@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private ProjectValidator projectValidator;

    @Test
    void testGetProjectWhenNotFound() throws Exception {
        // Given
        Optional<Project> expected = Optional.empty();

        Mockito.when(projectService.getProject(Mockito.anyInt())).thenReturn(expected);

        // When
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/project/1")
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // Then
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testPostProjectWhenNotValid() throws Exception {
        // Given
        ProjectAddDto dto = new ProjectAddDto();
        Mockito.when(projectValidator.isProjectAddDtoValid(Mockito.any())).thenReturn(false);

        // When
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
        );

        // Then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testPostProjectWhenValid() throws Exception {
        // Given
        ProjectAddDto dto = new ProjectAddDto();
        Project expected = new Project();
        expected.setId(5);
        expected.setName("test");
        Mockito.when(projectValidator.isProjectAddDtoValid(Mockito.any())).thenReturn(true);
        Mockito.when(projectService.saveProject(Mockito.any())).thenReturn(expected);

        // When
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.post("/v1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto))
        );

        // Then
        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(expected.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(expected.getName())));
    }

    @Test
    void testPutProjectWhenNotValid() throws Exception {
        // Given
        Project project = new Project();
        Mockito.when(projectValidator.isProjectValid(Mockito.any())).thenReturn(false);

        // When
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.put("/v1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(project))
        );

        // Then
        result.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testPutProjectWhenValid() throws Exception {
        // Given
        Project expected = new Project();
        expected.setId(5);
        expected.setName("test");
        Mockito.when(projectValidator.isProjectValid(Mockito.any())).thenReturn(true);
        Mockito.when(projectService.saveProject(Mockito.any())).thenReturn(expected);

        // When
        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.put("/v1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(expected))
        );

        // Then
        result
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(expected.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is(expected.getName())));
    }

}
