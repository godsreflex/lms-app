package com.example.LMS.web.controller;

import com.example.LMS.domain.task.Status;
import com.example.LMS.domain.task.Task;
import com.example.LMS.service.TaskService;
import com.example.LMS.service.UserService;
import com.example.LMS.web.DTO.external.AssignTaskToUserDTO;
import com.example.LMS.web.DTO.external.BooleanDTO;
import com.example.LMS.web.DTO.external.StatusDTO;
import com.example.LMS.web.DTO.external.StringDTO;
import com.example.LMS.web.DTO.mapper.TaskMapper;
import com.example.LMS.web.DTO.task.TaskDTO;
import com.example.LMS.web.DTO.task.TaskToUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    private final TaskMapper taskMapper;

    @GetMapping("/by-user-id/{userId}")
    public List<TaskDTO> getAllTasksByUserId(@PathVariable Long userId) {
        List<Task> tasks = taskService.getByUserId(userId);
        return taskMapper.toDTO(tasks);
    }

    @GetMapping("/by-assigner-id/{assignerId}")
    public List<TaskDTO> getAllTasksByAssignerId(@PathVariable Long assignerId) {
        List<Task> tasks = taskService.getByAssignerId(assignerId);
        return taskMapper.toDTO(tasks);
    }

    @GetMapping("/{taskId}/assigned-to/{userId}")
    public TaskDTO getPersonalTask(@PathVariable Long taskId, @PathVariable Long userId) {
        Task task = taskService.getByTaskIdAndUserId(taskId, userId);
        return taskMapper.toDTO(task);
    }

    @GetMapping("{taskId}")
    public List<TaskDTO> getAllTasksByTaskId(@PathVariable Long taskId) {
        List<Task> tasks = taskService.getByTaskId(taskId);
        return taskMapper.toDTO(tasks);
    }

    @GetMapping("/statuses/{taskId}")
    public List<StatusDTO> getStatus(@PathVariable Long taskId) {
        HashMap<Long, Status> statusesHashMap = taskService.getTasksStatuses(taskId);
        List<StatusDTO> statuses = new ArrayList<>();
        for (int i = 0; i < statusesHashMap.size(); i++) {
            Long userId = (Long) statusesHashMap.keySet().toArray()[i];
            String username = userService.getById(userId).getUsername();
            statuses.add(new StatusDTO(username, statusesHashMap.get(userId)));
        }
        return statuses;
    }

    @PutMapping("/{taskId}/{userId}/update-status")
    public BooleanDTO updateStatus(@RequestBody StringDTO stringDTO,
                                   @PathVariable Long taskId, @PathVariable Long userId) {
        taskService.updateStatus(stringDTO.getValue(), taskId, userId);
        return new BooleanDTO(true);
    }

    @PostMapping("/{assignerId}")
    public TaskDTO createTask(@PathVariable Long assignerId) {
        Task task = taskService.createTask(assignerId);
        return taskMapper.toDTO(task);
    }

    @PostMapping("/assign")
    public BooleanDTO assignTask(@RequestBody AssignTaskToUserDTO assignTaskToUserDTO) {
        taskService.assignTo(assignTaskToUserDTO.getTaskId(),
                userService.getByUsername(assignTaskToUserDTO.getUsername()).getId(),
                Status.AWAITING);
        return new BooleanDTO(true);
    }

    @PutMapping
    public TaskDTO updateTask(@RequestBody TaskToUpdateDTO taskToUpdateDTO) {
        Task task = new Task();
        task.setId(taskToUpdateDTO.getId());
        task.setTitle(taskToUpdateDTO.getTitle());
        task.setDescription(taskToUpdateDTO.getDescription());
        task.setExpirationDate(LocalDateTime.parse(taskToUpdateDTO.getExpirationDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        task.setAssignedByUserId(taskToUpdateDTO.getAssignedByUserId());

        Task updatedTask = taskService.updateTask(task);
        return taskMapper.toDTO(updatedTask);
    }

}
