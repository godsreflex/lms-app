package com.example.LMS.repository.implementation;

import com.example.LMS.domain.exception.ResourceNotFoundException;
import com.example.LMS.domain.task.Test;
import com.example.LMS.repository.DataSourceConnectionFactory;
import com.example.LMS.repository.TestRepository;
import com.example.LMS.repository.mapper.TestRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TestRepositoryImplementation implements TestRepository {

    private final DataSourceConnectionFactory dataSourceConnectionFactory;

    private final String ADD = """
            insert into tests (input, output, task_id) values (?, ?, ?)
            """;

    private final String FIND_BY_TASK_ID = """
            select t.id as tests_id,
                   t.input as tests_input,
                   t.output as tests_output
            from tests t
            where t.task_id = ?
            """;

    @Override
    public void addTest(Test test, Long taskId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(ADD);
            statement.setString(1, test.getInput());
            statement.setString(2, test.getOutput());
            statement.setLong(3, taskId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    @Override
    public List<Test> findTests(Long taskId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_TASK_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, taskId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return TestRowMapper.mapRows(resultSet);
            }
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

}
