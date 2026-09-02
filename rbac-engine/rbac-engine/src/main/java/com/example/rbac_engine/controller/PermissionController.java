package com.example.rbac_engine.controller;

import com.example.rbac_engine.dto.request.permission_request.PermissionRequest;
import com.example.rbac_engine.dto.response.permission_response.PermissionResponse;
import com.example.rbac_engine.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@Validated
public class PermissionController {

    private final PermissionService permissionService;

    @PreAuthorize("hasPermission(null, 'CREATE_PERMISSION')")
    @PostMapping
    public ResponseEntity<PermissionResponse> addPermission(@Valid @RequestBody PermissionRequest permissionRequest){
        PermissionResponse response = permissionService.createPermission(permissionRequest);
        URI location = URI.create("/api/permissions/"+response.getId());
        return ResponseEntity.created(location).body(response);
    }

}
