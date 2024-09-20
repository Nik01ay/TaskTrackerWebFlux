package com.example.tasktracker.service;


import com.example.tasktracker.entity.User;
import com.example.tasktracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public Flux<User> findAll() {
        return userRepository.findAll();
    }

    public Flux<User> findAllById(Iterable<String> strings) {return userRepository.findAllById(strings);}

    public Mono<User> findById(String id) {
        return userRepository.findById(id);
    }

    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    public Mono<User> update(String id, User user) {
        return userRepository.findById(id).flatMap(updateUser -> {
            if(StringUtils.hasText(user.getEmail())) {
                updateUser.setEmail(user.getEmail());
            }
            if(StringUtils.hasText(user.getUsername())) {
                updateUser.setUsername(user.getUsername());
            }

            return userRepository.save(updateUser);
        });
    }

    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<Void> deleteById(String id) {
        return userRepository.deleteById(id);
    }
}
