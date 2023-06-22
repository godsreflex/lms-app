package com.example.LMS.web.controller;

import com.example.LMS.domain.user.User;
import com.example.LMS.service.AuthService;
import com.example.LMS.service.UserService;
import com.example.LMS.web.DTO.auth.Credentials;
import com.example.LMS.web.DTO.auth.LoginResponse;
import com.example.LMS.web.DTO.external.StringDTO;
import com.example.LMS.web.DTO.mapper.UserMapper;
import com.example.LMS.web.DTO.user.UserDTO;
import com.example.LMS.web.DTO.validation.OnCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@Validated @RequestBody Credentials credentials) {
        return authService.login(credentials);
    }

    @PostMapping("/refresh")
    public LoginResponse refresh(@RequestBody StringDTO refreshToken) {
        return authService.refresh(refreshToken.getValue());
    }

}
