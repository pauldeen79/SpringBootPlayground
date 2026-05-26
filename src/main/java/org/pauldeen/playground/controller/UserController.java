package org.pauldeen.playground.controller;

import org.pauldeen.playground.model.dto.UserDTO;
import org.pauldeen.playground.model.entity.UserEntity;
import org.pauldeen.playground.model.exception.ResourceNotFoundException;
import org.pauldeen.playground.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/{id}", produces = "application/json")
    public UserDTO get(@PathVariable Long id) {
        return userService.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User with id %d could not be found".formatted(id)));
    }

    private UserDTO mapToDTO(UserEntity e) {
        return UserDTO.builder()
            .id(e.getId())
            .name(e.getUsername())
            .emailAddress(e.getEmail())
            .build();
    }
}
