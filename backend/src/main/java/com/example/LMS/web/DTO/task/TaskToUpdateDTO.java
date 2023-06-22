package com.example.LMS.web.DTO.task;

import com.example.LMS.domain.task.Status;
import lombok.Data;

@Data
public class TaskToUpdateDTO {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private String expirationDate;
    private Long assignedByUserId;

}
