package com.example.tasktracker.repository;

import com.example.tasktracker.entity.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.Set;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {

    Flux<Task> findAllByObserverIds(Set<String> observerIds);
}
