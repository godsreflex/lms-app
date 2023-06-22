package com.example.LMS.web.controller;

import com.example.LMS.service.SolutionService;
import com.example.LMS.web.DTO.external.BooleanDTO;
import com.example.LMS.web.DTO.mapper.SolutionMapper;
import com.example.LMS.web.DTO.solution.SolutionDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/solutions")
@RequiredArgsConstructor
public class SolutionController {

    private final SolutionService solutionService;

    private final SolutionMapper solutionMapper;

    @GetMapping("{userId}/{taskId}")
    public SolutionDTO getSolution(@PathVariable Long userId, @PathVariable Long taskId) {
        return solutionMapper.toDTO(solutionService.getSolution(userId, taskId));
    }

    @PostMapping
    public BooleanDTO createSolution(@RequestBody SolutionDTO solutionDTO) {
        solutionDTO.setPostingDate(LocalDateTime.now());
        solutionService.createSolution(solutionMapper.toEntity(solutionDTO));
        return new BooleanDTO(true);
    }

    @PutMapping
    public BooleanDTO updateSolution(@RequestBody SolutionDTO solutionDTO) {
        solutionService.updateSolution(solutionMapper.toEntity(solutionDTO));
        return new BooleanDTO(true);
    }

}
