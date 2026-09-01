package com.example.rbac_engine.mapper;

import com.example.rbac_engine.dto.request.user_request.UserRequest;
import com.example.rbac_engine.dto.response.user_response.UserResponse;
import com.example.rbac_engine.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public User toEntity(UserRequest request){
        if (request == null) return null;
        return User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
    }

    public UserResponse toDto(User user){
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
}
