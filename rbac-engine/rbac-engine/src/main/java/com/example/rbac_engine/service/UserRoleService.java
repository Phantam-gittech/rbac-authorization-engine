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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleMapper userRoleMapper;

    @Transactional
    public UserRoleResponse assignRoleToUser(Long userId, Long roleId){
        log.debug("assignRoleToUser call : user id = {}, role id = {}",userId, roleId);
        User user = userRepository.findById(userId)
                .orElseThrow(()->{
                    log.warn("assignRoleToUser failed : user not found with id = {}",userId);
                    return new ResourceNotFoundException("User not found with id = "+userId);
                });

        Role role = roleRepository.findById(roleId)
                .orElseThrow(()->{
                    log.warn("assignRoleToUser failed : role not found with id = {}",roleId);
                    return new ResourceNotFoundException("Role not found with id = "+roleId);
                });

        UserRole userRole = userRoleMapper.toEntity(user, role);
        UserRole savedUserRole = userRoleRepository.save(userRole);

        log.info("User = username : {} successfully assigned a role name : {}", savedUserRole.getUser().getUsername(), savedUserRole.getRole().getName());
        return userRoleMapper.toDto(savedUserRole);
    }

}
