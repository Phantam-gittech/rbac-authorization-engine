package com.example.rbac_engine.dto.response.user_role_response;

import com.example.rbac_engine.dto.response.role_response.RoleResponse;
import com.example.rbac_engine.dto.response.user_response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRoleResponse {
    private Long id;
    private UserResponse userResponse;
    private RoleResponse roleResponse;
}
