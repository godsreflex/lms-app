package com.example.LMS.repository.mapper;

import com.example.LMS.domain.task.Status;
import com.example.LMS.domain.task.Task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TaskRowMapper {

    public static Task mapRow(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            Task task = new Task();
            task.setId(resultSet.getLong("task_id"));
            task.setTitle(resultSet.getString("task_title"));
            task.setDescription(resultSet.getString("task_description"));
            task.setExpirationDate(resultSet.getTimestamp("task_expiration_date").toLocalDateTime());
            task.setAssignedByUserId(resultSet.getLong("task_assigned_by_id"));

            String status = resultSet.getString("user_tasks_task_status");
            if (status == null) {
                task.setStatus(null);
            } else {
                task.setStatus(Status.valueOf(status));
            }

            return task;
        }
        return null;
    }

    public static List<Task> mapRows(ResultSet resultSet) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        while (resultSet.next()) {
            Task task = new Task();
            task.setId(resultSet.getLong("task_id"));
            if (!resultSet.wasNull()) {
                task.setTitle(resultSet.getString("task_title"));
                task.setDescription(resultSet.getString("task_description"));

                for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                    if (resultSet.getMetaData().getColumnLabel(i + 1).equals("user_tasks_task_status")
                            && resultSet.getString("user_tasks_task_status") != null) {
                        task.setStatus(Status.valueOf(resultSet.getString("user_tasks_task_status")));
                    }
                }

                Timestamp timestamp = resultSet.getTimestamp("task_expiration_date");
                if (timestamp != null) {
                    task.setExpirationDate(timestamp.toLocalDateTime());
                }
                task.setAssignedByUserId(resultSet.getLong("task_assigned_by_id"));
                tasks.add(task);
            }
        }
        return tasks;
    }

}
