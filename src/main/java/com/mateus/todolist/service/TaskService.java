package com.mateus.todolist.service;

import com.mateus.todolist.controller.TaskController;
import com.mateus.todolist.domain.Task;
import com.mateus.todolist.dto.TaskDto;
import com.mateus.todolist.exception.TaskNotFoundException;
import com.mateus.todolist.mapper.TaskMapper;
import com.mateus.todolist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskMapper taskMapper;

    public List<Task> findAll() {
        var listTask = taskRepository.findAll();
        listTask = listTask.stream()
                .map((x) -> x.add
                        (linkTo(methodOn(TaskController.class).getOneTask(x.getId())).withSelfRel())
                ).collect(Collectors.toList());
        return listTask;
    }
    
    public Task findById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty())
            throw new TaskNotFoundException();
        Task task = optionalTask.get();
        task.add(linkTo(methodOn(TaskController.class).getAll()).withRel("List of tasks"));
        return task;
    }


    public Task save(TaskDto taskDto) {
        var task = taskMapper.toTask(taskDto);
        return taskRepository.save(task);
    }

    public Task update(Long id, TaskDto taskDto) {
        findById(id); // throws TaskNotFound to validate that the task exists
        Task taskToUpdate = taskMapper.toTask(taskDto);
        taskToUpdate.setId(id);
        return taskRepository.save(taskToUpdate);
    }

    public void delete(Long id) {
        var task = findById(id);
        taskRepository.delete(task);
    }
}
