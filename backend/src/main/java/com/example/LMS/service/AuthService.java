package com.example.LMS.service;

import com.example.LMS.web.DTO.auth.Credentials;
import com.example.LMS.web.DTO.auth.LoginResponse;

public interface AuthService {

    LoginResponse login(Credentials credentials);

    LoginResponse refresh(String refreshToken);

}
