package com.mateus.todolist.service;

import com.mateus.todolist.domain.Task;
import com.mateus.todolist.domain.enums.TaskStatus;
import com.mateus.todolist.dto.TaskDto;
import com.mateus.todolist.dto.TaskPageDto;
import com.mateus.todolist.exception.TaskNotFoundException;
import com.mateus.todolist.mapper.TaskMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
class TaskServiceTest {
    @Autowired
    TaskService taskService;

    @Autowired
    EntityManager entityManager;

    @Autowired
    TaskMapper taskMapper;

    protected Task createTask(TaskDto taskDto) {
        var newTask = taskMapper.toTask(taskDto);
        entityManager.persist(newTask);
        return newTask;
    }

    @Test
    @Transactional
    @DisplayName("Should get Task successfully from DB")
    void findByIdSuccess() {
        TaskDto taskDto = new TaskDto("Learn Java", "desc", TaskStatus.IN_PROGRESS);
        Task task = createTask(taskDto);
        Task foundedTask = taskService.findById(task.getId());
        assertEquals(foundedTask, task);
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when id is not valid")
    void findByIdInvalidId() {
        assertThrows(TaskNotFoundException.class, () -> taskService.findById(-1L));
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when don't have any Task with the passed id")
    void findByIdThrowsTaskNotFoundException() {
        assertThrows(TaskNotFoundException.class, () -> taskService.findById(12398123L));
    }

    @Test
    @DisplayName("Should correctly save a task in the DB")
    void saveSuccess() {
        TaskDto taskDto = new TaskDto("Learn Spring", "desc", TaskStatus.IN_PROGRESS);
        Task savedTask = taskService.save(taskDto);
        assertEquals(taskDto, taskMapper.toTaskDto(savedTask));
    }

    @Test
    @Transactional
    @DisplayName("Should update a task successfully")
    void updateSuccess() {
        Task createdTask = createTask(new TaskDto("Learn JUnit", "desc", TaskStatus.IN_PROGRESS));
        TaskDto newTask = new TaskDto("Learn JUnit", "desc", TaskStatus.DONE);
        Task updatedTask = taskService.update(createdTask.getId(), newTask);
        assertEquals(taskMapper.toTaskDto(updatedTask), newTask);
    }



    @Test
    @DisplayName("Should throw TaskNotFoundException when don't have any task with the passed id to delete")
    void deleteThrowsTaskNotFoundException() {
        assertThrows(TaskNotFoundException.class, () -> taskService.delete(12094302L));
    }


    @Test @Transactional
    @DisplayName("Should get a TaskPageDto if only one task")
    void getTasksSuccess() {
        createTask(new TaskDto("Learn pagination", "desc", TaskStatus.DONE));
        TaskPageDto taskPageDto = taskService.getTasks(0, 10);
        assertEquals(taskPageDto.tasks().size(), 1);
        assertEquals(taskPageDto.totalPages(), 1);
        assertEquals(taskPageDto.totalElements(), 1);
    }
}