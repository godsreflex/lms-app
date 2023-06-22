package com.example.LMS.repository.implementation;

import com.example.LMS.domain.exception.ResourceNotFoundException;
import com.example.LMS.domain.solution.Solution;
import com.example.LMS.repository.DataSourceConnectionFactory;
import com.example.LMS.repository.SolutionRepository;
import com.example.LMS.repository.mapper.SolutionRowMapper;
import com.example.LMS.repository.mapper.TaskRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SolutionRepositoryImplementation implements SolutionRepository {

    private final DataSourceConnectionFactory dataSourceConnectionFactory;

    private final String FIND = """
            select us.posting_date as user_solutions_posting_date,
                   us.text as user_solutions_text
            from user_solutions us
            where us.user_task_id = ?
            """;

    private final String CREATE = """
            insert into user_solutions (posting_date, text, user_task_id) values (?, ?, ?)
            """;

    private final String UPDATE = """
            update user_solutions set posting_date = ?, text = ? where user_task_id = ?
            """;

    @Override
    public Optional<Solution> findSolution(Long userTaskId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, userTaskId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return Optional.ofNullable(SolutionRowMapper.mapRow(resultSet));
            }
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    @Override
    public void create(Solution solution, Long userTaskId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE);
            statement.setTimestamp(1, Timestamp.valueOf(solution.getPostingDate()));
            statement.setString(2, solution.getText());
            statement.setLong(3, userTaskId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    @Override
    public void update(Solution solution, Long userTaskId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setTimestamp(1, Timestamp.valueOf(solution.getPostingDate()));
            statement.setString(2, solution.getText());
            statement.setLong(3, userTaskId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }
}
