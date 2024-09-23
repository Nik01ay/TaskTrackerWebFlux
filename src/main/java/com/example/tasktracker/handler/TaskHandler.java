package com.example.tasktracker.handler;

import com.example.tasktracker.entity.Task;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskHandler {

    private final UserService userService;

    public Mono<Task> handleTask(Task task, Mono<Task> taskMonoWithId) {
        return processTask(task, taskMonoWithId);
    }

    public Mono<Task> handleTask(Task task) {
        return processTask(task, Mono.just(task));
    }

    public Mono<Task> handleTask(Mono<Task> taskMono) {
        return taskMono.flatMap(this::handleTask);
    }


    private Mono<Task> processTask(Task task, Mono<Task> taskMonoWithId) {

        Mono<User> authorMono = userService.findById(task.getAuthorId());
        Mono<User> assigneeMono = userService.findById(task.getAssigneeId());

        Flux<User> observersFlux = userService.findAllById(task.getObserverIds());
        Mono<List<User>> userObs = observersFlux.collectList();

        Mono<Task> taskMono = Mono.just(task);

        taskMono = Mono.zip(taskMono, authorMono, assigneeMono, taskMonoWithId, userObs).flatMap(
                data -> {
                    Task taskInZip = data.getT1();
                    taskInZip.setAuthor(data.getT2());
                    taskInZip.setAssignee(data.getT3());
                    if (data.getT4() != null) {
                        taskInZip.setId(data.getT4().getId());
                    }
                    taskInZip.setObservers(data.getT5().stream().collect(Collectors.toSet()));
                    return Mono.just(taskInZip);
                }).log();

        return taskMono;


    }


}
