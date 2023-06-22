package com.example.LMS.repository.implementation;

import com.example.LMS.domain.exception.ResourceNotFoundException;
import com.example.LMS.domain.user.User;
import com.example.LMS.repository.DataSourceConnectionFactory;
import com.example.LMS.repository.UserRepository;
import com.example.LMS.repository.mapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImplementation implements UserRepository {

    private final DataSourceConnectionFactory dataSourceConnectionFactory;

    private final String FIND_BY_ID = """
            select u.id as user_id,
                   u.name as user_name,
                   u.username as user_username,
                   u.password as user_password,
                   u.role as user_role
            from users u
                   where u.id = ?
            """;

    private final String FIND_BY_USERNAME = """
            select u.id as user_id,
                   u.name as user_name,
                   u.username as user_username,
                   u.password as user_password,
                   u.role as user_role
            from users u
                   where u.username = ?
            """;

    private final String FIND_BY_ROLE = """
            select u.id as user_id,
                   u.name as user_name,
                   u.username as user_username,
                   u.password as user_password,
                   u.role as user_role
            from users u
                   where u.role = ?
            """;

    private final String FIND_BY_TASK_ID = """
            select u.id as user_id,
                   u.name as user_name,
                   u.username as user_username,
                   u.password as user_password,
                   u.role as user_role
            from users u
                   left join user_tasks ut on u.id = ut.user_id
                   left join tasks t on ut.task_id = t.id
            where t.id = ?;
            """;

    @Override
    public Optional<User> findById(Long id) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(resultSet));
            }
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while executing query: " + exception.getMessage());
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(resultSet));
            }
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while executing query: " + exception.getMessage());
        }
    }

    @Override
    public List<User> findByRole(String role) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ROLE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setString(1, role);
            try (ResultSet resultSet = statement.executeQuery()) {
                return UserRowMapper.mapRows(resultSet);
            }
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    @Override
    public List<User> findByTaskId(Long taskId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_TASK_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, taskId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return UserRowMapper.mapRows(resultSet);
            }
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query: " + exception.getMessage());
        }
    }

}
