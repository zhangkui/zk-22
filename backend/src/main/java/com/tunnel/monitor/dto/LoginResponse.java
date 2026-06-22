package com.tunnel.monitor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    private String token;

    private String tokenType;

    private Long expiresIn;

    private Long userId;

    private String username;

    private String realName;

    private String role;
}
