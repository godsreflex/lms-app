package com.example.LMS.repository.mapper;

import com.example.LMS.domain.comment.Comment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentRowMapper {

    public static List<Comment> mapRows(ResultSet resultSet) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        while (resultSet.next()) {
            Comment comment = new Comment();
            if (!resultSet.wasNull()) {
                comment.setId(resultSet.getLong("comment_id"));
                comment.setTaskId(resultSet.getLong("comment_task_id"));
                comment.setUserId(resultSet.getLong("comment_user_id"));
                comment.setText(resultSet.getString("comment_text"));
                comment.setCreatedDate(resultSet.getTimestamp("comment_creating_date").toLocalDateTime());
                comments.add(comment);
            }
        }
        return comments;
    }

}
