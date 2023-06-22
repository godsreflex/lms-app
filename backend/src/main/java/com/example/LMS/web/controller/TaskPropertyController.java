package com.example.LMS.web.controller;

import com.example.LMS.service.TaskPropertiesService;
import com.example.LMS.web.DTO.external.BooleanDTO;
import com.example.LMS.web.DTO.mapper.TaskPropertiesMapper;
import com.example.LMS.web.DTO.task.TaskPropertiesDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task-props")
@RequiredArgsConstructor
public class TaskPropertyController {

    private final TaskPropertiesService taskPropertiesService;

    private final TaskPropertiesMapper taskPropertiesMapper;

    @GetMapping("{taskId}")
    public TaskPropertiesDTO getTaskProperties(@PathVariable Long taskId) {
        TaskPropertiesDTO taskPropertiesDTO = taskPropertiesMapper.toDTO(taskPropertiesService.getTaskProperties(taskId));
        taskPropertiesDTO.setTaskId(taskId);
        return taskPropertiesDTO;
    }

    @PostMapping
    public BooleanDTO addTaskProperties(@RequestBody TaskPropertiesDTO taskPropertiesDTO) {
        taskPropertiesService.addTaskProperties(taskPropertiesMapper.toEntity(taskPropertiesDTO));
        return new BooleanDTO(true);
    }

    @PutMapping
    public BooleanDTO updateTaskProperties(@RequestBody TaskPropertiesDTO taskPropertiesDTO) {
        taskPropertiesService.updateTaskProperties(taskPropertiesMapper.toEntity(taskPropertiesDTO));
        return new BooleanDTO(true);
    }

}
