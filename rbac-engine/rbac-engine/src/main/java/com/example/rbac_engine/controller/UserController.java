package com.example.rbac_engine.controller;

import com.example.rbac_engine.dto.response.user_role_response.UserRoleResponse;
import com.example.rbac_engine.service.UserRoleService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserRoleService userRoleService;

    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<UserRoleResponse> assignRoleToUser(
            @Positive(message = "Invalid ID format!") @PathVariable Long userId,
            @Positive(message = "Invalid ID format!") @PathVariable Long roleId)
    {
        UserRoleResponse response = userRoleService.assignRoleToUser(userId, roleId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
