package com.example.rbac_engine.mapper;

import com.example.rbac_engine.dto.response.role_permission_response.RolePermissionResponse;
import com.example.rbac_engine.entity.Permission;
import com.example.rbac_engine.entity.Role;
import com.example.rbac_engine.entity.operations.RolePermission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RolePermissionMapper {
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    public RolePermission toEntity(Role role, Permission permission) {
        if (role == null || permission == null) return null;

        return RolePermission.builder()
                .role(role)
                .permission(permission)
                .build();
    }

    public RolePermissionResponse toDto(RolePermission rolePermission){
        if (rolePermission == null) return null;

        return RolePermissionResponse.builder()
                .id(rolePermission.getId())
                .roleResponse(roleMapper.toDto(rolePermission.getRole()))
                .permissionResponse(permissionMapper.toDto(rolePermission.getPermission()))
                .build();
    }
}
