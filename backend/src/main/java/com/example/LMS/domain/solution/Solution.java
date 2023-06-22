package com.example.LMS.domain.solution;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Solution {

    private Long id;
    private LocalDateTime postingDate;
    private String text;
    private Long userId;
    private Long taskId;

}
