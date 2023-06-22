package com.example.LMS.repository;

import com.example.LMS.domain.task.TaskProperties;

import java.util.Optional;

public interface TaskPropertiesRepository {

    Optional<TaskProperties> findByTaskId(Long taskId);

    void add(TaskProperties taskProperties);

    void update(TaskProperties taskProperties);

}
