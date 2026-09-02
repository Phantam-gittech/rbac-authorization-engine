package com.example.rbac_engine.service;

import com.example.rbac_engine.dto.response.user_role_response.UserRoleResponse;
import com.example.rbac_engine.entity.Role;
import com.example.rbac_engine.entity.User;

import com.example.rbac_engine.entity.operations.UserRole;
import com.example.rbac_engine.exception.ResourceNotFoundException;
import com.example.rbac_engine.mapper.UserRoleMapper;
import com.example.rbac_engine.repository.RoleRepository;
import com.example.rbac_engine.repository.UserRepository;
import com.example.rbac_engine.repository.UserRoleRepository;
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
class UserRoleServiceTest {

    @Mock private UserRoleRepository userRoleRepository;
    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private UserRoleMapper userRoleMapper;

    @InjectMocks
    private UserRoleService userRoleService;

    @Test
    void assignRoleToUser_succeeds_whenBothExist() {
        User user = User.builder().id(1L).username("atharva").build();
        Role role = Role.builder().id(2L).name("ADMIN").build();
        UserRole entity = UserRole.builder().id(10L).user(user).role(role).build();
        UserRoleResponse expected = UserRoleResponse.builder().id(10L).build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(role));
        when(userRoleMapper.toEntity(user, role)).thenReturn(entity);
        when(userRoleRepository.save(entity)).thenReturn(entity);
        when(userRoleMapper.toDto(entity)).thenReturn(expected);

        UserRoleResponse result = userRoleService.assignRoleToUser(1L, 2L);

        assertThat(result.getId()).isEqualTo(10L);
        verify(userRoleRepository).save(entity);
    }

    @Test
    void assignRoleToUser_throws_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userRoleService.assignRoleToUser(1L, 2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("1");

        verify(roleRepository, never()).findById(any());
        verify(userRoleRepository, never()).save(any());
    }

    @Test
    void assignRoleToUser_throws_whenRoleNotFound() {
        User user = User.builder().id(1L).username("atharva").build();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userRoleService.assignRoleToUser(1L, 2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("2");

        verify(userRoleRepository, never()).save(any());
    }
}