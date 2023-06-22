package com.example.LMS.web.DTO.external;

import com.example.LMS.domain.task.Status;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusDTO {

    private String username;
    private Status taskStatus;

}
