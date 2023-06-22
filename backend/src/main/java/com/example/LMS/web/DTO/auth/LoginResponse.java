package com.example.LMS.web.DTO.auth;

import lombok.Data;

@Data
public class LoginResponse {

    private Long id;
    private String username;
    private String accessToken;
    private String refreshToken;

}
