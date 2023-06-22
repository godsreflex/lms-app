package com.example.LMS.repository;

import com.example.LMS.domain.task.Status;
import com.example.LMS.domain.task.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    List<Task> findByUserId(Long userId);

    List<Task> findByAssignerId(Long assignerId);

    Optional<Task> findByTaskIdAndUserId(Long taskId, Long userId);

    List<Task> findByTaskId(Long taskId);

    Task create(Long assignerId);

    Task update(Task task);

    HashMap<Long, Status> findTaskStatuses(Long taskId);

    void assignTo(Long taskId, Long userId, Status status);

    Optional<Long> findUserTaskId(Long userId, Long taskId);

    void updateStatus(String status, Long taskId, Long userId);

}
