package com.example.LMS.web.DTO.verification;

import lombok.Data;

@Data
public class VerificationResultDTO {

    private String received;
    private String expected;
    private Boolean result;
    private Long testId;

}
