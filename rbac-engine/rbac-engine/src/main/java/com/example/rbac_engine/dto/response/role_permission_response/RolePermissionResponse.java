package com.example.rbac_engine.dto.response.role_permission_response;

import com.example.rbac_engine.dto.response.permission_response.PermissionResponse;
import com.example.rbac_engine.dto.response.role_response.RoleResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolePermissionResponse {
    private Long id;
    private RoleResponse roleResponse;
    private PermissionResponse permissionResponse;
}
