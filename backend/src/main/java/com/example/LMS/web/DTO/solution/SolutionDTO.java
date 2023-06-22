package com.example.LMS.web.DTO.solution;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SolutionDTO {

    private LocalDateTime postingDate;
    private String text;
    private Long userId;
    private Long taskId;

}
