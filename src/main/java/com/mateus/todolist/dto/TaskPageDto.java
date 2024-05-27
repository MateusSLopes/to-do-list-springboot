package com.mateus.todolist.dto;

import com.mateus.todolist.domain.Task;

import java.util.List;

public record TaskPageDto(List<Task> tasks, int totalPages, long totalElements) {
}
