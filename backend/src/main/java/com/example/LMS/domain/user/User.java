package com.example.LMS.domain.user;

import com.example.LMS.domain.task.Task;
import lombok.Data;

@Data
public class User {

    private Long id;
    private String name;
    private String username;
    private String password;
    private Role role;

}
