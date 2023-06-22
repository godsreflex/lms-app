package com.example.LMS.service.implementation;

import com.example.LMS.domain.exception.ResourceNotFoundException;
import com.example.LMS.domain.task.TaskProperties;
import com.example.LMS.repository.TaskPropertiesRepository;
import com.example.LMS.service.TaskPropertiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskPropertiesServiceImplementation implements TaskPropertiesService {

    private final TaskPropertiesRepository taskPropertiesRepository;

    @Transactional(readOnly = true)
    @Override
    public TaskProperties getTaskProperties(Long taskId) {
        return taskPropertiesRepository.findByTaskId(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task properties not found"));
    }

    @Transactional
    @Override
    public void addTaskProperties(TaskProperties taskProperties) {
        taskPropertiesRepository.add(taskProperties);
    }

    @Override
    public void updateTaskProperties(TaskProperties taskProperties) {
        taskPropertiesRepository.update(taskProperties);
    }
}
