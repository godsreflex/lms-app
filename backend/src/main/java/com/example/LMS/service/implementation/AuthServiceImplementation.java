package com.example.LMS.service.implementation;

import com.example.LMS.domain.user.User;
import com.example.LMS.service.AuthService;
import com.example.LMS.service.UserService;
import com.example.LMS.web.DTO.auth.Credentials;
import com.example.LMS.web.DTO.auth.LoginResponse;
import com.example.LMS.web.security.JWTProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JWTProvider jwtProvider;

    @Override
    public LoginResponse login(Credentials credentials) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                credentials.getUsername(),
                credentials.getPassword()
        ));
        User user = userService.getByUsername(credentials.getUsername());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setId(user.getId());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setAccessToken(jwtProvider.createAccessToken(user.getId(), user.getUsername(), user.getRole()));
        loginResponse.setRefreshToken(jwtProvider.createRefreshToken(user.getId(), user.getUsername()));
        return loginResponse;
    }

    @Override
    public LoginResponse refresh(String refreshToken) {
        return jwtProvider.doRefreshToken(refreshToken);
    }

}
