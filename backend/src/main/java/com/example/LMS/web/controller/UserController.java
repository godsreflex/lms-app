package com.example.LMS.web.controller;

import com.example.LMS.domain.user.User;
import com.example.LMS.service.UserService;
import com.example.LMS.web.DTO.external.BooleanDTO;
import com.example.LMS.web.DTO.mapper.UserMapper;
import com.example.LMS.web.DTO.user.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return userMapper.toDTO(user);
    }

    @GetMapping("/{userId}/is-task-owner/{taskId}")
    public BooleanDTO isTaskOwner(@PathVariable Long userId, @PathVariable Long taskId) {
        return new BooleanDTO(userService.isTaskOwner(userId, taskId));
    }

    @GetMapping("/by-role/{role}")
    public List<UserDTO> getByRole(@PathVariable String role) {
        List<User> users = userService.getByRole(role);
        return userMapper.toDTO(users);
    }

    @GetMapping("/{taskId}/assigned-to")
    public List<UserDTO> getByTaskId(@PathVariable Long taskId) {
        List<User> users = userService.getByTaskId(taskId);
        return userMapper.toDTO(users);
    }

}
