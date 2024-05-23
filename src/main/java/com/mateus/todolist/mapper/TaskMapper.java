package com.mateus.todolist.mapper;

import com.mateus.todolist.domain.Task;
import com.mateus.todolist.dto.TaskDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toTask(TaskDto taskDto);
    TaskDto toTaskDto(Task task);
}
