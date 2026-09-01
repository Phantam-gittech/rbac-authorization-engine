package com.example.rbac_engine.service;

import com.example.rbac_engine.dto.request.permission_request.PermissionRequest;
import com.example.rbac_engine.dto.response.permission_response.PermissionResponse;
import com.example.rbac_engine.entity.Permission;
import com.example.rbac_engine.exception.DataConflictException;
import com.example.rbac_engine.mapper.PermissionMapper;
import com.example.rbac_engine.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Transactional
    public PermissionResponse createPermission(PermissionRequest permissionRequest){
        log.debug("createPermission call: permission name = {}",permissionRequest.getName());
        if (permissionRepository.existsByNameIgnoreCase(permissionRequest.getName())){
            log.warn("createPermission rejected : permission already exists with name = {}",permissionRequest.getName());
            throw new DataConflictException("Permission already exists with name = "+permissionRequest.getName());
        }

        Permission permission = permissionMapper.toEntity(permissionRequest);
        Permission savedPermission = permissionRepository.save(permission);

        log.info("new PERMISSION created successfully : id = {}, name = {}",savedPermission.getId(), savedPermission.getName());
        return permissionMapper.toDto(savedPermission);
    }
}
