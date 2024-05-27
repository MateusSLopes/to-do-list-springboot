package com.mateus.todolist.controller;

import com.mateus.todolist.domain.Task;
import com.mateus.todolist.dto.TaskDto;
import com.mateus.todolist.dto.TaskPageDto;
import com.mateus.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/tasks")
@RestController
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping
    public ResponseEntity<TaskPageDto> getTasks(Pageable pageable) {
        TaskPageDto page = taskService.getTasks(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getOneTask(@PathVariable("id") Long id) {
        Task task = this.taskService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @PostMapping
    public ResponseEntity<Task> saveTask(@RequestBody @Valid TaskDto taskDto) {
        Task savedTask = this.taskService.save(taskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") Long id, @RequestBody @Valid TaskDto taskDto) {
        Task updatedTask = this.taskService.update(id, taskDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") Long id) {
        this.taskService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully");
    }


}