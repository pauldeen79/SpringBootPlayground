package org.pauldeen.playground.service;

import java.util.Optional;

import org.pauldeen.playground.model.entity.UserEntity;
import org.pauldeen.playground.repository.UserRepository;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UserService {
    private final UserRepository repository;

    public Optional<UserEntity> findById(Long id) {
        return repository.findById(id);
    }
}
