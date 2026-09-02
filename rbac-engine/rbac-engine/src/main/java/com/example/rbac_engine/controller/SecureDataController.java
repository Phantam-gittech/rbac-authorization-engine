package com.example.rbac_engine.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SecureDataController {

    @PreAuthorize("hasPermission(null, 'VIEW_SECURE_DATA')")
    @GetMapping("/secure-data")
    public ResponseEntity<String> getSecureData() {
        return ResponseEntity.ok("This is protected data, visible only with the correct permission.");
    }
}
