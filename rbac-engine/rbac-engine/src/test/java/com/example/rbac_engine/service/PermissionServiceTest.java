package com.example.rbac_engine.service;

import com.example.rbac_engine.dto.request.permission_request.PermissionRequest;
import com.example.rbac_engine.dto.response.permission_response.PermissionResponse;
import com.example.rbac_engine.entity.Permission;
import com.example.rbac_engine.exception.DataConflictException;
import com.example.rbac_engine.mapper.PermissionMapper;
import com.example.rbac_engine.repository.PermissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private PermissionMapper permissionMapper;

    @InjectMocks
    private PermissionService permissionService;

    @Test
    void createPermission_savesAndReturnsPermission_whenNameIsNew() {
        PermissionRequest request = new PermissionRequest();
        request.setName("CREATE_ROLE");

        Permission permissionEntity = Permission.builder().name("CREATE_ROLE").build();
        Permission savedPermission = Permission.builder().id(1L).name("CREATE_ROLE").build();
        PermissionResponse expectedResponse = PermissionResponse.builder().id(1L).name("CREATE_ROLE").build();

        when(permissionRepository.existsByNameIgnoreCase("CREATE_ROLE")).thenReturn(false);
        when(permissionMapper.toEntity(request)).thenReturn(permissionEntity);
        when(permissionRepository.save(permissionEntity)).thenReturn(savedPermission);
        when(permissionMapper.toDto(savedPermission)).thenReturn(expectedResponse);

        PermissionResponse result = permissionService.createPermission(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("CREATE_ROLE");
        verify(permissionRepository).save(permissionEntity);
    }

    @Test
    void createPermission_throwsDataConflictException_whenNameAlreadyExists() {
        PermissionRequest request = new PermissionRequest();
        request.setName("CREATE_ROLE");

        when(permissionRepository.existsByNameIgnoreCase("CREATE_ROLE")).thenReturn(true);

        assertThatThrownBy(() -> permissionService.createPermission(request))
                .isInstanceOf(DataConflictException.class)
                .hasMessageContaining("CREATE_ROLE");

        verify(permissionRepository, never()).save(any());
    }
}