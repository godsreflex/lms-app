package com.example.LMS.repository;

import com.example.LMS.domain.exception.ResourceNotFoundException;
import com.example.LMS.domain.verification.VerificationResult;
import com.example.LMS.repository.mapper.TestRowMapper;
import com.example.LMS.repository.mapper.VerificationResultRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class VerificationRepository {

    private final DataSourceConnectionFactory dataSourceConnectionFactory;

    private final String FIND_ALL = """
            select vr.received as verif_results_received,
                   vr.expected as verif_results_expected,
                   vr.result as verif_results_result,
                   vr.test_id as verif_results_test_id
            from verification_results as vr
            where vr.user_id = ? and vr.task_id = ?
            """;

    private final String UPDATE = """
            update verification_results set received = ?, expected = ?, result = ?
                    where test_id = ? and user_id = ? and task_id = ?
            """;

    private final String CREATE = """
            insert into verification_results (received, expected, result, test_id, user_id, task_id)
                    values (?, ?, ?, ?, ?, ?)
            """;

    public List<VerificationResult> findAllResults(Long userId, Long taskId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_ALL,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, userId);
            statement.setLong(2, taskId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return VerificationResultRowMapper.mapRows(resultSet);
            }
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    public void update(VerificationResult result, Long userId, Long taskId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, result.getReceived());
            statement.setString(2, result.getExpected());
            statement.setBoolean(3, result.getResult());
            statement.setLong(4, result.getTestId());
            statement.setLong(5, userId);
            statement.setLong(6, taskId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    public void create(VerificationResult result, Long userId, Long taskId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE);
            statement.setString(1, result.getReceived());
            statement.setString(2, result.getExpected());
            statement.setBoolean(3, result.getResult());
            statement.setLong(4, result.getTestId());
            statement.setLong(5, userId);
            statement.setLong(6, taskId);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

}
