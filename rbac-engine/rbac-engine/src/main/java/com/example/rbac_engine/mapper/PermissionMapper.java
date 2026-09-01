package com.example.rbac_engine.mapper;

import com.example.rbac_engine.dto.request.permission_request.PermissionRequest;
import com.example.rbac_engine.dto.response.permission_response.PermissionResponse;
import com.example.rbac_engine.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    public Permission toEntity(PermissionRequest request){
        if (request == null) return null;
        return Permission.builder()
                .name(request.getName())
                .build();
    }

    public PermissionResponse toDto(Permission permission){
        if (permission == null) return null;
        return PermissionResponse.builder()
                .id(permission.getId())
                .name(permission.getName())
                .build();
    }
}
