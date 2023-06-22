package com.example.LMS.web.controller;


import com.example.LMS.domain.comment.Comment;
import com.example.LMS.service.CommentService;
import com.example.LMS.web.DTO.comment.CommentDTO;
import com.example.LMS.web.DTO.external.BooleanDTO;
import com.example.LMS.web.DTO.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @PostMapping
    public BooleanDTO postComment(@RequestBody CommentDTO commentDTO) {
        Comment comment = commentMapper.toEntity(commentDTO);
        comment.setCreatedDate(LocalDateTime.now());
        commentService.create(comment);
        return new BooleanDTO(true);
    }

    @GetMapping("/{taskId}/{userId}")
    public List<Comment> getComment(@PathVariable Long taskId, @PathVariable Long userId) {
        return commentService.getComments(taskId, userId);
    }
}
