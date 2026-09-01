package com.example.rbac_engine.repository;

import com.example.rbac_engine.entity.operations.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
