package com.example.LMS.service.implementation;

import com.example.LMS.domain.exception.ResourceNotFoundException;
import com.example.LMS.domain.task.Status;
import com.example.LMS.domain.task.Task;
import com.example.LMS.repository.TaskRepository;
import com.example.LMS.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImplementation implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Task> getByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getByAssignerId(Long assignerId) {
        return taskRepository.findByAssignerId(assignerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Task getByTaskIdAndUserId(Long taskId, Long userId) {
        return taskRepository.findByTaskIdAndUserId(taskId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getByTaskId(Long taskId) {
        return taskRepository.findByTaskId(taskId);
    }

    @Override
    @Transactional
    public Task createTask(Long assignerId) {
        Task task = taskRepository.create(assignerId);
        task.setAssignedByUserId(assignerId);
        return task;
    }

    @Override
    @Transactional
    public Task updateTask(Task task) {
        return taskRepository.update(task);
    }

    @Override
    @Transactional(readOnly = true)
    public HashMap<Long, Status> getTasksStatuses(Long taskId) {
        return taskRepository.findTaskStatuses(taskId);
    }

    @Override
    @Transactional
    public void assignTo(Long taskId, Long userId, Status status) {
        taskRepository.assignTo(taskId, userId, status);
    }

    @Override
    @Transactional
    public void updateStatus(String status, Long taskId, Long userId) {
        taskRepository.updateStatus(status, taskId, userId);
    }

}
