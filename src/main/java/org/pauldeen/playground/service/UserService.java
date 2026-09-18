package org.pauldeen.playground.service;

import java.util.Optional;

import org.pauldeen.playground.model.entity.UserEntity;
import org.pauldeen.playground.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service 
public class UserService {
    private final UserRepository repository;

    public Optional<UserEntity> findById(Long id) {
        return repository.findById(id);
    }

    public UserEntity save(UserEntity user) {
        return repository.saveAndFlush(user);
    }

    public void delete(UserEntity user) {
        repository.delete(user);
    }
}
