package com.example.LMS.service;

import com.example.LMS.domain.task.Status;
import com.example.LMS.domain.task.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskService {

    List<Task> getByUserId(Long userId);

    List<Task> getByAssignerId(Long assignerId);

    Task getByTaskIdAndUserId(Long taskId, Long userId);

    List<Task> getByTaskId(Long taskId);

    Task createTask(Long assignerId);

    Task updateTask(Task task);

    HashMap<Long, Status> getTasksStatuses(Long taskId);

    void assignTo(Long taskId, Long userId, Status status);

    void updateStatus(String status, Long taskId, Long userId);

}
