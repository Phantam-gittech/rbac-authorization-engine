package com.example.rbac_engine.repository;

import com.example.rbac_engine.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User>  findByUsername(String username);
}
