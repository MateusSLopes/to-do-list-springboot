package com.mateus.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateus.todolist.domain.Task;
import com.mateus.todolist.domain.enums.TaskStatus;
import com.mateus.todolist.dto.TaskDto;
import com.mateus.todolist.dto.TaskPageDto;
import com.mateus.todolist.service.TaskService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class TaskControllerTest {
    @InjectMocks
    TaskController controller;

    @Mock
    private TaskService service;

    Task task;

    @Autowired
    ObjectMapper objectMapper;

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
    }

    @Test
    @DisplayName("Should return a task in the response content with Http status OK")
    void getOneTaskSuccess() throws Exception {
        when(service.findById(1L)).thenReturn(task);

        mockMvc.perform(
                        get("/tasks/{id}", "1")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"id\":1,\"title\":\"name of task\",\"description\":\"desc\",\"taskStatus\":\"DONE\",\"links\":[]}"));

        verify(service).findById(1L);
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Should return a task page with a list of tasks in the response content")
    void getAllTasksWithDefaultValues() throws Exception {
        var taskPageDto = new TaskPageDto(Collections.singletonList(task), 1, 1);
        when(service.getTasks(0, 10)).thenReturn(taskPageDto);

        mockMvc.perform(get("/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"tasks\":[{\"id\":1,\"title\":\"name of task\",\"description\":\"desc\",\"taskStatus\":\"DONE\",\"links\":[]}],\"totalPages\":1,\"totalElements\":1}"));

        verify(service).getTasks(0, 10);
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Should return a task page with a list of tasks in the response content, where the page and pageSize are passed as a parameter")
    void getAllTasksWithParamValues() throws Exception {
        var taskPageDto = new TaskPageDto(Collections.singletonList(task), 1, 1);
        when(service.getTasks(0, 90)).thenReturn(taskPageDto);

        MockHttpServletRequestBuilder request = get("/tasks").param("page", "0")
                .param("pageSize", "90");
        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("{\"tasks\":[{\"id\":1,\"title\":\"name of task\",\"description\":\"desc\",\"taskStatus\":\"DONE\",\"links\":[]}],\"totalPages\":1,\"totalElements\":1}"));

        verify(service).getTasks(0, 90);
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Should save a task with success and return the task in the response content")
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
                .andExpect(content().string(objectMapper.writeValueAsString(returnedTask)));

        verify(service).save(taskDto);
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Should delete a task successfully")
    void deleteTaskSuccess() throws Exception{

        mockMvc.perform(delete("/tasks/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string("Task deleted successfully"));

        verify(service).delete(1L);
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Should update a task successfully and return the new task in the response content")
    void updateTaskSuccess() throws Exception {
        var newTaskDto = new TaskDto("Updated", "Up", TaskStatus.IN_PROGRESS);
        var newTask = Task.TaskBuilder.builder()
                .id(1L)
                .title(newTaskDto.title())
                .description(newTaskDto.description())
                .taskStatus(newTaskDto.taskStatus())
                .build();

        when(service.update(1L, newTaskDto)).thenReturn(newTask);

        mockMvc.perform(put("/tasks/{id}", 1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newTaskDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(newTask)));

        verify(service).update(1L, newTaskDto);
        verifyNoMoreInteractions(service);
    }
}