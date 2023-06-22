package com.example.LMS.web.DTO.mapper;

import com.example.LMS.domain.comment.Comment;
import com.example.LMS.web.DTO.comment.CommentDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toEntity(CommentDTO commentDTO);

}
