package com.example.LMS.web.controller;

import com.example.LMS.domain.task.Test;
import com.example.LMS.service.TestService;
import com.example.LMS.web.DTO.external.BooleanDTO;
import com.example.LMS.web.DTO.mapper.TestMapper;
import com.example.LMS.web.DTO.task.TestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    private final TestMapper testsMapper;

    @GetMapping("{taskId}")
    public List<TestDTO> getTests(@PathVariable Long taskId) {
        List<Test> tests = testService.getTests(taskId);
        return testsMapper.toDTO(tests);
    }

    @PostMapping("{taskId}")
    public BooleanDTO addTests(@RequestBody List<TestDTO> testsDTO, @PathVariable Long taskId) {
        testService.addTests(testsMapper.toEntity(testsDTO), taskId);
        return new BooleanDTO(true);
    }
}
