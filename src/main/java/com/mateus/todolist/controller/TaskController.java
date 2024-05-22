package com.mateus.todolist.controller;

import com.mateus.todolist.domain.Task;
import com.mateus.todolist.dto.TaskDto;
import com.mateus.todolist.service.TaskService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
    @Autowired
    TaskService taskService;

    @GetMapping("/task")
    public ResponseEntity<List<Task>> getAll() {
        List<Task> listTask = this.taskService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(listTask);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Task> getOneTask(@PathVariable("id") Long id) {
        Task task = this.taskService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @PostMapping("/task")
    public ResponseEntity<Task> saveTask(@RequestBody @Valid TaskDto taskDto) {
        Task savedTask = this.taskService.save(taskDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTask);
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable("id") Long id, @RequestBody @Valid TaskDto taskDto) {
        Task updatedTask = this.taskService.update(id, taskDto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") Long id) {
        this.taskService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Task deleted successfully");
    }
}
