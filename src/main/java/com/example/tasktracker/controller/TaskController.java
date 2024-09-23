package com.example.tasktracker.controller;

import com.example.tasktracker.mapper.TaskMapper;
import com.example.tasktracker.model.TaskModel;
import com.example.tasktracker.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @GetMapping
    public Flux<TaskModel> getAll(){
        System.out.println("GET ALL");
        return taskService.findAll()
                .map(taskMapper::taskToResponse);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<TaskModel>> getById(@PathVariable String id) {

        return taskService.findById(id)
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<TaskModel>> create(@RequestBody TaskModel taskModel) {
        System.out.println("POST");
        System.out.println(taskModel.toString());
        return taskService.save(taskMapper.requestToTask(taskModel))
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<TaskModel>> update(@PathVariable String id, @RequestBody TaskModel taskModel) {
        return taskService.update(id, taskMapper.requestToTask(taskModel))
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/{user-id}")
    public Mono<ResponseEntity<TaskModel>> addObserver(@PathVariable String id,
                                                       @PathVariable("user-id") String userId) {
        return taskService.addObserver(id, userId)
                .map(taskMapper::taskToResponse)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable String id) {
        return taskService.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
