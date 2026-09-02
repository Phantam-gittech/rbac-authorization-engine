package com.example.rbac_engine.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Covers the remaining 3 required endpoints at the HTTP/security layer,
 * completing full-API @PreAuthorize coverage alongside
 * RoleControllerSecurityTest (roles + secure-data).
 * Uses the real Spring context + data.sql seed (admin/ADMIN role with
 * all permissions, IDs 1) — same pattern already verified working.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RemainingEndpointsSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    // ---- POST /api/permissions ----

    @Test
    void addPermission_withoutAuth_isRejected() throws Exception {
        mockMvc.perform(post("/api/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UNAUTHORIZED_PERM\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void addPermission_withValidAdminCredentials_isCreated() throws Exception {
        mockMvc.perform(post("/api/permissions")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"PERM_FROM_CONTROLLER_TEST\"}"))
                .andExpect(status().isCreated());
    }

    // ---- POST /api/roles/{roleId}/permissions/{permissionId} ----

    @Test
    void assignPermissionToRole_withoutAuth_isRejected() throws Exception {
        mockMvc.perform(post("/api/roles/1/permissions/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void assignPermissionToRole_withValidAdminCredentials_isCreated() throws Exception {
        mockMvc.perform(post("/api/roles/1/permissions/1")
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isCreated());
    }

    // ---- POST /api/users/{userId}/roles/{roleId} ----

    @Test
    void assignRoleToUser_withoutAuth_isRejected() throws Exception {
        mockMvc.perform(post("/api/users/1/roles/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void assignRoleToUser_withValidAdminCredentials_isCreated() throws Exception {
        mockMvc.perform(post("/api/users/1/roles/1")
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isCreated());
    }
}