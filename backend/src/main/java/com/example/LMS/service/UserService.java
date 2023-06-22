package com.example.LMS.service;

import com.example.LMS.domain.user.User;

import java.util.List;

public interface UserService {

    User getById(Long id);

    User getByUsername(String username);

    Boolean isTaskOwner(Long userId, Long taskId);

    List<User> getByRole(String role);

    List<User> getByTaskId(Long taskId);

}
