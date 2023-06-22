package com.example.LMS.repository.implementation;

import com.example.LMS.domain.comment.Comment;
import com.example.LMS.domain.exception.ResourceNotFoundException;
import com.example.LMS.repository.CommentRepository;
import com.example.LMS.repository.DataSourceConnectionFactory;
import com.example.LMS.repository.mapper.CommentRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImplementation implements CommentRepository {

    private final DataSourceConnectionFactory dataSourceConnectionFactory;


    private final String CREATE = """
            insert into comments(user_id, task_id, text, creating_date)
            values (?, ?, ?, ?)
            """;

    private final String FIND_COMMENTS = """
            select c.id as comment_id,
                   c.user_id as comment_user_id,
                   c.task_id as comment_task_id,
                   c.text as comment_text,
                   c.creating_date as comment_creating_date
            from comments c
                   where c.user_id = ? and c.task_id = ?
            """;

    @Override
    public void createComment(Comment comment) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE);
            statement.setLong(1, comment.getUserId());
            statement.setLong(2, comment.getTaskId());
            statement.setString(3, comment.getText());
            statement.setTimestamp(4, Timestamp.valueOf(comment.getCreatedDate()));
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

    @Override
    public List<Comment> findComments(Long taskId, Long userId) {
        try {
            Connection connection = dataSourceConnectionFactory.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_COMMENTS,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, userId);
            statement.setLong(2, taskId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return CommentRowMapper.mapRows(resultSet);
            }
        } catch (SQLException exception) {
            throw new ResourceNotFoundException("Error while SQL query" + exception.getMessage());
        }
    }

}
