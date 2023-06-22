package com.example.LMS.domain.verification;

import lombok.Data;

@Data
public class VerificationResult {

    private String received;
    private String expected;
    private Boolean result;
    private Long testId;

}
