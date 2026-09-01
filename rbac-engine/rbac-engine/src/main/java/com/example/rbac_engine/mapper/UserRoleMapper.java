package com.example.rbac_engine.mapper;

import com.example.rbac_engine.dto.response.user_role_response.UserRoleResponse;
import com.example.rbac_engine.entity.Role;
import com.example.rbac_engine.entity.User;
import com.example.rbac_engine.entity.operations.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRoleMapper {
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    public UserRole toEntity(User user, Role role){
        if (user == null || role == null) return null;
        return UserRole.builder()
                .user(user)
                .role(role)
                .build();
    }

    public UserRoleResponse toDto(UserRole userRole){
        if (userRole == null) return null;
        return UserRoleResponse.builder()
                .id(userRole.getId())
                .userResponse(userMapper.toDto(userRole.getUser()))
                .roleResponse(roleMapper.toDto(userRole.getRole()))
                .build();
    }
}
