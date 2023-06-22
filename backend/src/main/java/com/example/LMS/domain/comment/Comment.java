package com.example.LMS.domain.comment;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {

    private Long id;
    private Long userId;
    private Long taskId;
    private String text;
    private LocalDateTime createdDate;

}
