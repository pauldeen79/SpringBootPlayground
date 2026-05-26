package org.pauldeen.playground.repository;

import org.pauldeen.playground.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
}
