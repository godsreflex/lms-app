package com.example.LMS.service;

import com.example.LMS.domain.task.TaskProperties;

public interface TaskPropertiesService {

    TaskProperties getTaskProperties(Long taskId);

    void addTaskProperties(TaskProperties taskProperties);

    void updateTaskProperties(TaskProperties taskProperties);

}
