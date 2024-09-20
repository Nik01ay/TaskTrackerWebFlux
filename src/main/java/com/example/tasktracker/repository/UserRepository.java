package com.example.tasktracker.repository;

import com.example.tasktracker.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    @Override
    Flux<User> findAllById(Iterable<String> strings);

    Mono<User> findByUsername(String username);
}
