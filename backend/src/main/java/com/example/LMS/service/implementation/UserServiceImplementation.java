package com.example.LMS.service.implementation;

import com.example.LMS.domain.exception.ResourceNotFoundException;
import com.example.LMS.domain.task.Task;
import com.example.LMS.domain.user.User;
import com.example.LMS.repository.UserRepository;
import com.example.LMS.service.TaskService;
import com.example.LMS.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    private final TaskService taskService;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public Boolean isTaskOwner(Long userId, Long taskId) {
        for (Task task : taskService.getByUserId(userId)) {
            if (task.getId() == taskId) return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getByRole(String role) {
        return userRepository.findByRole(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getByTaskId(Long taskId) {
        return userRepository.findByTaskId(taskId);
    }

}
