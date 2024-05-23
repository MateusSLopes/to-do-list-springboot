package com.mateus.todolist.domain;

import com.mateus.todolist.domain.enums.TaskStatus;
import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

@Entity(name = "task")
@Table(name = "tb_task")
public class Task extends RepresentationModel<Task> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 70)
    private String title;

    @Column(length = 255)
    private String description;

    @Column(name = "task_status") @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }


    public static final class TaskBuilder {
        private Long id;
        private String title;
        private String description;
        private TaskStatus taskStatus;

        private TaskBuilder() {
        }

        public static TaskBuilder builder() {
            return new TaskBuilder();
        }

        public TaskBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TaskBuilder title(String title) {
            this.title = title;
            return this;
        }

        public TaskBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TaskBuilder taskStatus(TaskStatus taskStatus) {
            this.taskStatus = taskStatus;
            return this;
        }

        public Task build() {
            Task task = new Task();
            task.setId(id);
            task.setTitle(title);
            task.setDescription(description);
            task.setTaskStatus(taskStatus);
            return task;
        }
    }
}
