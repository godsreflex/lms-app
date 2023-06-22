package com.example.LMS.service;

import com.example.LMS.domain.comment.Comment;

import java.util.List;

public interface CommentService {

    void create(Comment comment);

    List<Comment> getComments(Long taskId, Long userId);

}
