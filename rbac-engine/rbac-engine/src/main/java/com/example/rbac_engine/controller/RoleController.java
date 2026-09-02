package com.example.rbac_engine.controller;

import com.example.rbac_engine.dto.request.role_request.RoleRequest;
import com.example.rbac_engine.dto.response.role_permission_response.RolePermissionResponse;
import com.example.rbac_engine.dto.response.role_response.RoleResponse;
import com.example.rbac_engine.service.RolePermissionService;
import com.example.rbac_engine.service.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Validated
public class RoleController {
    private final RoleService roleService;
    private final RolePermissionService rolePermissionService;

    @PreAuthorize("hasPermission(null, 'CREATE_ROLE')")
    @PostMapping
    public ResponseEntity<RoleResponse> addRole(@Valid @RequestBody RoleRequest roleRequest){
        RoleResponse response = roleService.createRole(roleRequest);
        URI location = URI.create("/api/roles/"+response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PreAuthorize("hasPermission(null, 'ASSIGN_PERMISSION_TO_ROLE')")
    @PostMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<RolePermissionResponse> assignPermission(
            @Positive(message = "Invalid ID format") @PathVariable Long roleId,
            @Positive(message = "Invalid ID format") @PathVariable Long permissionId
    ){
        RolePermissionResponse response = rolePermissionService.assignPermissionToRole(roleId, permissionId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
