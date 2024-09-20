package com.example.tasktracker.mapper;

import com.example.tasktracker.entity.Task;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.model.TaskModel;
import com.example.tasktracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.HashSet;

public abstract class TaskMapperDelegate implements TaskMapper {

    @Override
    public Task requestToTask(TaskModel request) {
        Task task = new Task();
        task.setId(request.getId());
        task.setName(request.getName());
        task.setDescription(request.getDescription());

        if (request.getCreatedAt() != null) {
            task.setCreatedAt(request.getCreatedAt());
        } else {
            task.setCreatedAt(null);
        }

        if (request.getUpdatedAt() != null) {
            task.setUpdatedAt(request.getUpdatedAt());
        } else {
            task.setUpdatedAt(null);
        }

        task.setAuthorId(request.getAuthorId());

        task.setAssigneeId(request.getAssigneeId());

        if (request.getObserverIds() != null) {
            task.setObserverIds(request.getObserverIds());
        } else {
            task.setObserverIds(new HashSet<>());
        }

        return task;
    }

    @Override
    public Task requestToTask(Long taskId, TaskModel request) {
        Task task = requestToTask(request);

        return task;
    }
}
