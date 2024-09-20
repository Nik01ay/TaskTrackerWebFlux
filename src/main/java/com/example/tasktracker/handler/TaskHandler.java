package com.example.tasktracker.handler;

import com.example.tasktracker.entity.Task;
import com.example.tasktracker.entity.User;
import com.example.tasktracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class TaskHandler {

    private final UserService userService;

    public Mono<Task> handleTask(Task task, Mono<Task> taskMonoWithId) {
        return processTask(task, taskMonoWithId);
    }

    public Mono<Task> handleTask(Task task) {
        return processTask(task, Mono.empty());
    }

    public Mono<Task> handleTask(Mono<Task> taskMono) {
        return taskMono.flatMap(this::handleTask);
    }


    private Mono<Task> processTask(Task task, Mono<Task> taskMonoWithId) {
        Mono<User> authorMono = userService.findById(task.getAuthorId());
        Mono<User> assigneeMono = userService.findById(task.getAssigneeId());
        Set<String> observersIds = task.getObserverIds();

        Flux<User> userFlux = userService.findAllById(observersIds);
        Set<User> userSet = new HashSet<>();
        Flux<Set<User>> userSetFlux = userFlux.flatMap(users -> {
            userSet.add(users);
            return Mono.just(userSet);
        });

        Mono<Task> taskMono = Mono.just(task);

        taskMono = Mono.zip(taskMono, authorMono, assigneeMono, taskMonoWithId).flatMap(
                data -> {
                    Task taskInZip = data.getT1();
                    taskInZip.setAuthor(data.getT2());
                    taskInZip.setAssignee(data.getT3());

                    if (data.getT4() != null) {
                        taskInZip.setId(data.getT4().getId());
                    }

                    return Mono.just(taskInZip);
                });
        return Flux.zip(taskMono, userSetFlux).flatMap(
                data -> {
                    Task task1 = data.getT1();
                    task1.setObservers(data.getT2());

                    return Mono.just(task1);
                }
        ).next();
    }
}
