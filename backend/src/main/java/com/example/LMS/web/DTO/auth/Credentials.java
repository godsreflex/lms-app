package com.example.LMS.web.DTO.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Credentials {

    @NotNull(message = "Username must be not null")
    @NotBlank(message = "Username must be not blank")
    private String username;

    @NotNull(message = "Password must be not null")
    @NotBlank(message = "Password must be not blank")
    private String password;

}
