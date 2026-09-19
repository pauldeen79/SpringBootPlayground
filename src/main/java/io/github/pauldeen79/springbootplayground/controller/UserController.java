package io.github.pauldeen79.springbootplayground.controller;

import io.github.pauldeen79.springbootplayground.model.dto.UserDTO;
import io.github.pauldeen79.springbootplayground.model.entity.UserEntity;
import io.github.pauldeen79.springbootplayground.model.exception.ResourceNotFoundException;
import io.github.pauldeen79.springbootplayground.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO userDTO) {
        UserEntity entity = mapToEntity(userDTO);
        UserEntity savedEntity = userService.save(entity);
        return new ResponseEntity<>(mapToDTO(savedEntity), HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public UserDTO get(@PathVariable Long id) {
        return userService.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("User with id %d could not be found".formatted(id)));
    }

    @PutMapping(value = "/{id}", produces = "application/json", consumes = "application/json")
    public UserDTO update(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserEntity entity = mapToEntity(userDTO);
        entity.id(id);
        UserEntity updatedEntity = userService.save(entity);
        return mapToDTO(updatedEntity);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.findById(id)
                .ifPresentOrElse(userService::delete,
                    () -> new ResourceNotFoundException("User with id %d could not be found".formatted(id)));
        return ResponseEntity.noContent().build();
    }

    private UserDTO mapToDTO(UserEntity e) {
        return UserDTO.builder()
            .id(e.id())
            .name(e.username())
            .emailAddress(e.email())
            .build();
    }

    private UserEntity mapToEntity(UserDTO dto) {
        return new UserEntity()
            .id(dto.getId())
            .username(dto.getName())
            .email(dto.getEmailAddress());
    }
}
