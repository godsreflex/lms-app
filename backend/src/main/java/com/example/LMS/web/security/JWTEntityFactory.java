package com.example.LMS.web.security;

import com.example.LMS.domain.user.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;

public class JWTEntityFactory {

    public static JWTEntity create(User user) {
        return new JWTEntity(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getPassword(),
                new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())))
        );
    }

}
