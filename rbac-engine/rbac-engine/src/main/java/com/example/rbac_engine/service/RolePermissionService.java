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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RolePermissionService {
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionMapper rolePermissionMapper;


    @Transactional
    public RolePermissionResponse assignPermissionToRole(Long roleId, Long permissionId){
        log.debug("assignPermissionToRole call : role id = {}, permission id = {}",roleId, permissionId);
        Role role = roleRepository.findById(roleId)
                .orElseThrow(()->{
                    log.warn("assignPermissionToRole Failed : ROLE not found with id = {}",roleId);
                    return new ResourceNotFoundException("Role not found with id = "+roleId);
                });

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(()->{
                    log.warn("assignPermissionToRole Failed : PERMISSION not found with id = {}",permissionId);
                    return new ResourceNotFoundException("Permission not found with id = "+permissionId);
                });

        RolePermission rolePermission = rolePermissionMapper.toEntity(role, permission);
        RolePermission saveRolePermission = rolePermissionRepository.save(rolePermission);

        log.info("Role successfully assigned to permission : role id = {}, role name = {} | permission id = {}, permission name = {}",
                saveRolePermission.getRole().getId(), saveRolePermission.getRole().getName(),
                saveRolePermission.getPermission().getId(), saveRolePermission.getPermission().getName());
        return rolePermissionMapper.toDto(saveRolePermission);
    }
}
