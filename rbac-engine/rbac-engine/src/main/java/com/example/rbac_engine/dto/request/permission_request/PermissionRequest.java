package com.example.rbac_engine.dto.request.permission_request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermissionRequest {
    @NotBlank(message = "Permission name required!")
    private String name;

    public void setName(String name){
        this.name = name != null
                ? name.trim()
                : null;
    }
}
