package com.example.LMS.repository.implementation;

import com.example.LMS.domain.exception.ResourceNotFoundException;
import com.example.LMS.domain.task.Status;
import com.example.LMS.domain.task.Task;
import com.example.LMS.repository.DataSourceConnectionFactory;
import com.example.LMS.repository.TaskRepository;
import com.example.LMS.repository.mapper.TaskRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryImplementation implements TaskRepository {

    private final DataSourceConnectionFactory dataSourceConnectionFactory;

    private final String FIND_BY_USER_ID = """
            select t.id as task_id,
                   t.title as task_title,
                   t.description as task_description,
                   t.expiration_date as task_expiration_date,
                   t.assigned_by_id as task_assigned_by_id,
                   ut.task_status as user_tasks_task_status
            from tasks t
                left join user_tasks ut on t.id = ut.task_id
            where ut.user_id = ?
            """;

    private final String FIND_BY_ID = """
            select t.id as task_id,
                   t.title as task_title,
                   t.description as task_description,
                   t.expiration_date as task_expiration_date,
                   ut.task_status as user_tasks_task_status,
                   t.assigned_by_id as task_assigned_by_id
            from tasks t
                left join user_tasks ut on t.id = ut.task_id
            where t.id = ?
    """;

    private final String FIND_BY_ASSIGNER_ID = """
            select t.id as task_id,
                   t.title as task_title,
                   t.description as task_description,
                   t.expiration_date as task_expiration_date,
                   t.assigned_by_id as task_assigned_by_id
            from tasks t
            where t.assigned_by_id = ?
            """;

    private final String FIND_BY_TASK_ID_AND_USER_ID = """
            select t.id as task_id,
                   t.title as task_title,
                   t.description as task_description,
                   t.expiration_date as task_expiration_date,
                   t.assigned_by_id as task_assigned_by_id,
                   ut.task_status as user_tasks_task_status
            from tasks t
                left join user_tasks ut on t.id = ut.task_id
            where ut.user_id = ? and ut.task_id = ?
            """;

    private final String CREATE = """
            insert into tasks(title, description, expiration_date, assigned_by_id)
            values (?, ?, ?, ?)
            """;

    private final String UPDATE = """
            update tasks set title = ?, description = ?, expiration_date = ?, assigned_by_id = ?
            where id = ?
            """;

    private final String FIND_TASKS_STATUSES_BY_TASK_ID = """
            select ut.user_id as user_tasks_user_id,
                   ut.task_status as user_tasks_task_status
            from user_tasks ut
            where ut.task_id = ? 
            """;

    private final String ASSIGN_TO = """
            insert into user_tasks(task_id, user_id, task_status)
            values (?, ?, ?)
            """;

    private final String UPDATE_STATUS = """
            update user_tasks set task_status = ?
            where task_id = ? and user_id = ?
            """;

    private final String FIND_USER_TASK_ID = """
            select ut.id as user_tasks_id
            from user_tasks ut
            where ut.user_id = ? and ut.task_id = ?
            """;

    @Override
    public List<Task> findByUserId(Long userId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USER_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return TaskRowMapper.mapRows(resultSet);
            }
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    @Override
    public List<Task> findByAssignerId(Long assignerId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ASSIGNER_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, assignerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return TaskRowMapper.mapRows(resultSet);
            }
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    @Override
    public Optional<Task> findByTaskIdAndUserId(Long taskId, Long userId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_TASK_ID_AND_USER_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, userId);
            statement.setLong(2, taskId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return Optional.ofNullable(TaskRowMapper.mapRow(resultSet));
            }
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    @Override
    public List<Task> findByTaskId(Long taskId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, taskId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return TaskRowMapper.mapRows(resultSet);
            }
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    @Override
    public Task create(Long assignerId) {
        try {
            Task task = new Task();
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setNull(1, Types.VARCHAR);
            statement.setNull(2, Types.VARCHAR);
            statement.setNull(3, Types.TIMESTAMP);
            statement.setLong(4, assignerId);
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                task.setId(resultSet.getLong(1));
            }
            return task;
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query"  + exception.getMessage());
        }
    }

    @Override
    public Task update(Task task) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());
            statement.setTimestamp(3, Timestamp.valueOf(task.getExpirationDate()));
            statement.setLong(4, task.getAssignedByUserId());
            statement.setLong(5, task.getId());
            statement.executeUpdate();
            return task;
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    @Override
    public HashMap<Long, Status> findTaskStatuses(Long taskId) {
        try {
            HashMap<Long, Status> statusesHashMap = new HashMap<>();
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_TASKS_STATUSES_BY_TASK_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, taskId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Long userId = resultSet.getLong("user_tasks_user_id");
                    Status status = Status.valueOf(resultSet.getString("user_tasks_task_status"));
                    statusesHashMap.put(userId, status);
                }
            }
            return statusesHashMap;
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query: " + exception.getMessage());
        }
    }

    @Override
    public void assignTo(Long taskId, Long userId, Status status) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(ASSIGN_TO);
            statement.setLong(1, taskId);
            statement.setLong(2, userId);
            statement.setString(3, status.name());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    @Override
    public Optional<Long> findUserTaskId(Long userId, Long taskId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_USER_TASK_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, userId);
            statement.setLong(2, taskId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return Optional.ofNullable(resultSet.getLong("user_tasks_id"));
                return null;
            }
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    @Override
    public void updateStatus(String status, Long taskId, Long userId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_STATUS);
            statement.setLong(2, taskId);
            statement.setLong(3, userId);
            statement.setString(1, status);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

}
