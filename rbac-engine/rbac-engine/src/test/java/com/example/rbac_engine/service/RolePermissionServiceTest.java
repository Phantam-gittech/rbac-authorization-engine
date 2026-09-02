package com.example.rbac_engine.service;


import com.example.rbac_engine.dto.response.role_permission_response.RolePermissionResponse;
import com.example.rbac_engine.entity.Permission;
import com.example.rbac_engine.entity.Role;

import com.example.rbac_engine.entity.operations.RolePermission;
import com.example.rbac_engine.exception.ResourceNotFoundException;
import com.example.rbac_engine.mapper.RolePermissionMapper;
import com.example.rbac_engine.repository.PermissionRepository;
import com.example.rbac_engine.repository.RolePermissionRepository;
import com.example.rbac_engine.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RolePermissionServiceTest {

    @Mock private RolePermissionRepository rolePermissionRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PermissionRepository permissionRepository;
    @Mock private RolePermissionMapper rolePermissionMapper;

    @InjectMocks
    private RolePermissionService rolePermissionService;

    @Test
    void assignPermissionToRole_succeeds_whenBothExist() {
        Role role = Role.builder().id(1L).name("ADMIN").build();
        Permission permission = Permission.builder().id(2L).name("CREATE_ROLE").build();
        RolePermission entity = RolePermission.builder().id(10L).role(role).permission(permission).build();
        RolePermissionResponse expected = RolePermissionResponse.builder().id(10L).build();

        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(permissionRepository.findById(2L)).thenReturn(Optional.of(permission));
        when(rolePermissionMapper.toEntity(role, permission)).thenReturn(entity);
        when(rolePermissionRepository.save(entity)).thenReturn(entity);
        when(rolePermissionMapper.toDto(entity)).thenReturn(expected);

        RolePermissionResponse result = rolePermissionService.assignPermissionToRole(1L, 2L);

        assertThat(result.getId()).isEqualTo(10L);
        verify(rolePermissionRepository).save(entity);
    }

    @Test
    void assignPermissionToRole_throws_whenRoleNotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rolePermissionService.assignPermissionToRole(1L, 2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("1");

        verify(permissionRepository, never()).findById(any());
        verify(rolePermissionRepository, never()).save(any());
    }

    @Test
    void assignPermissionToRole_throws_whenPermissionNotFound() {
        Role role = Role.builder().id(1L).name("ADMIN").build();
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(permissionRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> rolePermissionService.assignPermissionToRole(1L, 2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("2");

        verify(rolePermissionRepository, never()).save(any());
    }
}