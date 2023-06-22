package com.example.LMS.web.controller;

import com.example.LMS.domain.task.Test;
import com.example.LMS.domain.verification.VerificationResult;
import com.example.LMS.service.TestService;
import com.example.LMS.service.VerificationService;
import com.example.LMS.web.DTO.external.BooleanDTO;
import com.example.LMS.web.DTO.external.StringDTO;
import com.example.LMS.web.DTO.mapper.VerificationMapper;
import com.example.LMS.web.DTO.verification.VerificationRequestDTO;
import com.example.LMS.web.DTO.verification.VerificationResultDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final TestService testService;

    private final VerificationService verificationService;

    private final VerificationMapper verificationMapper;

    @GetMapping("/{taskId}/{userId}")
    public List<VerificationResultDTO> getResults(@PathVariable Long taskId, @PathVariable Long userId) {
        List<VerificationResult> results = verificationService.getResults(taskId, userId);
        return verificationMapper.toDTO(results);
    }

    @PostMapping
    // List<VerificationResultDTO>
    public Object run(@RequestBody VerificationRequestDTO verificationRequestDTO) {
        List<Test> tests = testService.getTests(verificationRequestDTO.getTaskId());
        List<VerificationResultDTO> verificationResultsDTO = new ArrayList<>();
        String error = "";
        for (Test test : tests) {
            verificationService.createFile(test, verificationRequestDTO.getSolutionText());
            error = verificationService.createDockerImage();
            if (!error.equals("")) {
                System.out.println("ERROR FOUND!!!");
                return new StringDTO(error);
            }
            String received = verificationService.startDockerContainer();

            VerificationResultDTO verificationResultDTO = new VerificationResultDTO();
            verificationResultDTO.setReceived(received);
            verificationResultDTO.setExpected(test.getOutput());
            verificationResultDTO.setTestId(test.getId());
            if (received.equals(test.getOutput())) {
                verificationResultDTO.setResult(true);
            } else {
                verificationResultDTO.setResult(false);
            }
            verificationResultsDTO.add(verificationResultDTO);
        }
        return verificationResultsDTO;
    }

    @PostMapping("{userId}/{taskId}")
    public BooleanDTO saveResults(@RequestBody List<VerificationResultDTO> verificationResultsDTO,
                                  @PathVariable Long userId,
                                  @PathVariable Long taskId) {
        verificationService.saveResults(verificationMapper.toEntity(verificationResultsDTO), userId, taskId);
        return new BooleanDTO(true);
    }

}
