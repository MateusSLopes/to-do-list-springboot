package com.mateus.todolist.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateus.todolist.domain.Task;
import com.mateus.todolist.domain.enums.TaskStatus;
import com.mateus.todolist.dto.TaskDto;
import com.mateus.todolist.dto.TaskPageDto;
import com.mateus.todolist.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {
    @InjectMocks
    TaskController controller;

    @Mock
    private TaskService service;

    Task task;

    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .alwaysDo(print())
                .build();
        task = Task.TaskBuilder.builder()
                .id(1L).title("name of task")
                .description("desc")
                .taskStatus(TaskStatus.DONE).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Should return a task with Http status OK")
    void getOneTaskSuccess() throws Exception {
        when(service.findById(1L)).thenReturn(task);

        mockMvc.perform(
                        get("/tasks/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(service).findById(1L);
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Should return a task page with a list of tasks")
    void getAllTasksWithDefaultValues() throws Exception {
        var taskPageDto = new TaskPageDto(Collections.singletonList(task), 1, 1);
        when(service.getTasks(0, 10)).thenReturn(taskPageDto);

        mockMvc.perform(get("/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(service).getTasks(0, 10);
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Should return a task page with a list of tasks, where the page and pageSize are passed as a parameter")
    void getAllTasksWithParamValues() throws Exception {
        var taskPageDto = new TaskPageDto(Collections.singletonList(task), 1, 1);
        when(service.getTasks(0, 90)).thenReturn(taskPageDto);

        MockHttpServletRequestBuilder request = get("/tasks").param("page", "0")
                .param("pageSize", "90");
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        verify(service).getTasks(0, 90);
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Should save a task with success")
    void saveTaskSuccess() throws Exception {
        var taskDto = new TaskDto("name", "description", TaskStatus.IN_PROGRESS);
        var returnedTask = Task.TaskBuilder.builder()
                .id(2L)
                .title(taskDto.title())
                .description(taskDto.description())
                .taskStatus(taskDto.taskStatus())
                .build();
        when(service.save(taskDto)).thenReturn(returnedTask);

        MockHttpServletRequestBuilder request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(taskDto));
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        verify(service).save(taskDto);
        verifyNoMoreInteractions(service);
    }
}