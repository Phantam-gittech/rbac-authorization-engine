package com.example.rbac_engine.service;

import com.example.rbac_engine.dto.request.role_request.RoleRequest;
import com.example.rbac_engine.dto.response.role_response.RoleResponse;
import com.example.rbac_engine.entity.Role;
import com.example.rbac_engine.exception.DataConflictException;
import com.example.rbac_engine.mapper.RoleMapper;
import com.example.rbac_engine.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Transactional
    public RoleResponse createRole(RoleRequest request){
        log.debug("createRole call : name = {}",request.getName());
        if (roleRepository.existsByNameIgnoreCase(request.getName())){
            log.warn("createRole rejected : Role already exist with the name : {}", request.getName());
            throw new DataConflictException("Role already exists with name : "+request.getName());
        }

        Role role = roleMapper.toEntity(request);
        Role savedRole = roleRepository.save(role);

        log.info("new ROLE added success : id = {}, name = {}",savedRole.getId(), savedRole.getName());
        return roleMapper.toDto(savedRole);
    }
}
