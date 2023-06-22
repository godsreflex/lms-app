package com.example.LMS.service.implementation;

import com.example.LMS.domain.comment.Comment;
import com.example.LMS.domain.exception.ResourceNotFoundException;
import com.example.LMS.repository.CommentRepository;
import com.example.LMS.repository.TaskRepository;
import com.example.LMS.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImplementation implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public void create(Comment comment) {
        commentRepository.createComment(comment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comment> getComments(Long taskId, Long userId) {
        return commentRepository.findComments(taskId, userId);
    }

}
