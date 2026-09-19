package io.github.pauldeen79.springbootplayground.repository;

import io.github.pauldeen79.springbootplayground.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
}
