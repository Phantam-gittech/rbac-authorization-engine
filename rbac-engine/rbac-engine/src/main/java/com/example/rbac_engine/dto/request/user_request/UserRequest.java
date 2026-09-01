package com.example.rbac_engine.dto.request.user_request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "Username required!")
    @Size(min = 2, max = 150, message = "Username must be between 2-150 characters")
    private String username;

    @NotBlank(message = "Password required!")
    @Size(min = 8, max = 32, message = "Password must be between 8-32 characters")
    private String password;

    public void setUsername(String username){
        this.username = username != null
                ? username.trim()
                : null;
    }
}
