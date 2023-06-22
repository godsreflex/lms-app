package com.example.LMS.repository.mapper;

import com.example.LMS.domain.task.TaskProperties;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskPropertiesRowMapper {

    public static TaskProperties mapRow(ResultSet resultSet) throws SQLException {
        TaskProperties taskProperties = new TaskProperties();
        if (resultSet.next()) {
            taskProperties.setTaskId(resultSet.getLong("task_props_task_id"));
            taskProperties.setCodeTemplate(resultSet.getString("task_props_code_template"));
        }
        return taskProperties;
    }

}
