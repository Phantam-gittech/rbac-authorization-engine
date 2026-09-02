package com.example.rbac_engine.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Full-stack security test: real Spring context, real H2 + data.sql seed,
 * real CustomPermissionEvaluator — proves @PreAuthorize actually blocks
 * unauthenticated requests and allows correctly-permissioned ones,
 * end-to-end through the HTTP layer (not just the service layer).
 */
@SpringBootTest
@AutoConfigureMockMvc
class RoleControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addRole_withoutAuth_isRejected() throws Exception {
        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"UNAUTHORIZED_ROLE\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void addRole_withValidAdminCredentials_isCreated() throws Exception {
        mockMvc.perform(post("/api/roles")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"ROLE_FROM_CONTROLLER_TEST\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void secureData_withoutAuth_isRejected() throws Exception {
        mockMvc.perform(get("/api/secure-data"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void secureData_withValidAdminCredentials_isOk() throws Exception {
        mockMvc.perform(get("/api/secure-data")
                        .with(httpBasic("admin", "admin123")))
                .andExpect(status().isOk());
    }
}