package com.mateus.todolist.dto;

import com.mateus.todolist.domain.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskDto (@NotBlank String title, @NotNull String description, @NotNull TaskStatus taskStatus) {
}
