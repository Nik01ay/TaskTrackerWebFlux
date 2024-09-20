package com.example.tasktracker.service;

import com.example.tasktracker.entity.Task;
import com.example.tasktracker.handler.TaskHandler;
import com.example.tasktracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskHandler taskHandler;

    public Flux<Task> findAll() {
        return taskRepository.findAll().flatMap(taskHandler::handleTask);
    }

    public Mono<Task> findById(String id) {
        Mono<Task> taskToFind = taskRepository.findById(id);

        return taskHandler.handleTask(taskToFind);
    }

    public Mono<Task> save(Task task) {
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());

        Mono<Task> taskToSave = taskRepository.save(task);

        return taskHandler.handleTask(task, taskToSave);
    }

    public Mono<Task> update(String id, Task task) {
        return taskRepository.findById(id).flatMap(taskForUpdate -> {
            taskForUpdate.setId(task.getId());
            taskForUpdate.setName(task.getName());
            taskForUpdate.setDescription(taskForUpdate.getDescription());
            taskForUpdate.setCreatedAt(task.getCreatedAt());
            taskForUpdate.setUpdatedAt(Instant.now());
            taskForUpdate.setStatus(task.getStatus());
            taskForUpdate.setAuthorId(task.getAuthorId());
            taskForUpdate.setAssigneeId(task.getAssigneeId());
            taskForUpdate.setObserverIds(task.getObserverIds());

            Mono<Task> taskToSave = taskRepository.save(task);

            return taskHandler.handleTask(task, taskToSave);
        });
    }

    public Mono<Task> addObserver(String taskId, String userId){
        return findById(taskId).flatMap(task -> {
            Set<String> observerIds = task.getObserverIds();
            observerIds.add(userId);
            task.setObserverIds(observerIds);

            Mono<Task> taskMono = taskRepository.save(task);
            return taskHandler.handleTask(taskMono);
        });
    }

    public Mono<Void> deleteById(String id) {
        return taskRepository.deleteById(id);
    }
}
