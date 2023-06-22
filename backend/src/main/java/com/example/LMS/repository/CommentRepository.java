package com.example.LMS.repository;

import com.example.LMS.domain.comment.Comment;

import java.util.List;

public interface CommentRepository {

    void createComment(Comment comment);

    List<Comment> findComments(Long taskId, Long userId);

}
