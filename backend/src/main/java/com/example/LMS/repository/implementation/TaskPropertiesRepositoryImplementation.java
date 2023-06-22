package com.example.LMS.repository.implementation;

import com.example.LMS.domain.exception.ResourceNotFoundException;
import com.example.LMS.domain.task.TaskProperties;
import com.example.LMS.repository.DataSourceConnectionFactory;
import com.example.LMS.repository.TaskPropertiesRepository;
import com.example.LMS.repository.mapper.TaskPropertiesRowMapper;
import com.example.LMS.repository.mapper.TestRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskPropertiesRepositoryImplementation implements TaskPropertiesRepository {

    private final DataSourceConnectionFactory dataSourceConnectionFactory;

    private final String FIND_BY_TASK_ID = """
            select tp.code_template as task_props_code_template,
                   tp.task_id as task_props_task_id 
            from task_properties tp
            where tp.task_id = ?
            """;

    private final String ADD = """
            insert into task_properties (code_template, task_id) values (?, ?)
            """;

    private final String UPDATE = """
            update task_properties set code_template = ? where task_id = ?
            """;

    @Override
    public Optional<TaskProperties> findByTaskId(Long taskId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_TASK_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, taskId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return Optional.ofNullable(TaskPropertiesRowMapper.mapRow(resultSet));
            }
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    @Override
    public void add(TaskProperties taskProperties) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(ADD);
            statement.setString(1, taskProperties.getCodeTemplate());
            statement.setLong(2, taskProperties.getTaskId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    @Override
    public void update(TaskProperties taskProperties) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, taskProperties.getCodeTemplate());
            statement.setLong(2, taskProperties.getTaskId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }
}
