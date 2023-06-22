package com.example.LMS.repository;

import com.example.LMS.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    List<User> findByRole(String role);

    List<User> findByTaskId(Long taskId);

}
