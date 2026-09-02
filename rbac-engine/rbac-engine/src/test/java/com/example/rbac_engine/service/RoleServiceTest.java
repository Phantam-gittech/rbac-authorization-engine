package com.example.rbac_engine.service;



import com.example.rbac_engine.dto.request.role_request.RoleRequest;
import com.example.rbac_engine.dto.response.role_response.RoleResponse;
import com.example.rbac_engine.entity.Role;
import com.example.rbac_engine.exception.DataConflictException;
import com.example.rbac_engine.mapper.RoleMapper;
import com.example.rbac_engine.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    @Test
    void createRole_savesAndReturnsRole_whenNameIsNew() {
        // Arrange
        RoleRequest request = new RoleRequest();
        request.setName("ADMIN");

        Role roleEntity = Role.builder().name("ADMIN").build();
        Role savedRole = Role.builder().id(1L).name("ADMIN").build();
        RoleResponse expectedResponse = RoleResponse.builder().id(1L).name("ADMIN").build();

        when(roleRepository.existsByNameIgnoreCase("ADMIN")).thenReturn(false);
        when(roleMapper.toEntity(request)).thenReturn(roleEntity);
        when(roleRepository.save(roleEntity)).thenReturn(savedRole);
        when(roleMapper.toDto(savedRole)).thenReturn(expectedResponse);

        // Act
        RoleResponse result = roleService.createRole(request);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("ADMIN");
        verify(roleRepository).save(roleEntity);
    }

    @Test
    void createRole_throwsDataConflictException_whenNameAlreadyExists() {
        // Arrange
        RoleRequest request = new RoleRequest();
        request.setName("ADMIN");

        when(roleRepository.existsByNameIgnoreCase("ADMIN")).thenReturn(true);

        // Act + Assert
        assertThatThrownBy(() -> roleService.createRole(request))
                .isInstanceOf(DataConflictException.class)
                .hasMessageContaining("ADMIN");

        verify(roleRepository, never()).save(any());
    }
}