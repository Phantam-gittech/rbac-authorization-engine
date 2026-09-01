package com.example.rbac_engine.repository;


import com.example.rbac_engine.entity.operations.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
}
