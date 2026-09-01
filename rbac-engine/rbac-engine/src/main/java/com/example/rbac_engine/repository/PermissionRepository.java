package com.example.rbac_engine.repository;

import com.example.rbac_engine.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
    boolean existsByNameIgnoreCase(String name);
}
