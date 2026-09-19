package io.github.pauldeen79.springbootplayground.service;

import java.util.Optional;

import io.github.pauldeen79.springbootplayground.model.entity.UserEntity;
import io.github.pauldeen79.springbootplayground.repository.UserRepository;
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
