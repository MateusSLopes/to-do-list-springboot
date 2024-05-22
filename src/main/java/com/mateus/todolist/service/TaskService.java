package com.mateus.todolist.service;

import com.mateus.todolist.domain.Task;
import com.mateus.todolist.dto.TaskDto;
import com.mateus.todolist.exception.TaskNotFoundException;
import com.mateus.todolist.repository.TaskRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }
    
    public Task findById(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty())
            throw new TaskNotFoundException();
        return optionalTask.get();
    }


    public Task save(TaskDto taskDto) {
        var task = new Task();
        BeanUtils.copyProperties(taskDto, task);
        return taskRepository.save(task);
    }

    public Task update(Long id, TaskDto taskDto) {
        Task taskToUpdate = findById(id);
        taskToUpdate.setDescription(taskDto.description());
        taskToUpdate.setTitle(taskDto.title());
        taskToUpdate.setTaskStatus(taskDto.taskStatus());
        return taskRepository.save(taskToUpdate);
    }

    public void delete(Long id) {
        var task = findById(id);
        taskRepository.delete(task);
    }
}
