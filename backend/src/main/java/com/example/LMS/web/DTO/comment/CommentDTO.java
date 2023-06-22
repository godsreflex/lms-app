package com.example.LMS.web.DTO.comment;

import lombok.Data;

@Data
public class CommentDTO {

    private Long userId;
    private Long taskId;
    private String text;

}
