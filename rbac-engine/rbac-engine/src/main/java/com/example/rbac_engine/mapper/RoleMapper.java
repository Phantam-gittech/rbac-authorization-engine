package com.example.rbac_engine.mapper;

import com.example.rbac_engine.dto.request.role_request.RoleRequest;
import com.example.rbac_engine.dto.response.role_response.RoleResponse;
import com.example.rbac_engine.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toEntity(RoleRequest roleRequest){
        if (roleRequest == null) return null;

        return Role.builder()
                .name(roleRequest.getName())
                .build();
    }

    public RoleResponse toDto(Role role){
        if (role == null) return null;
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
