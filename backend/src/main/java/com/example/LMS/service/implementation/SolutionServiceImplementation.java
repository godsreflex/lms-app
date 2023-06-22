package com.example.LMS.service.implementation;

import com.example.LMS.domain.exception.ResourceNotFoundException;
import com.example.LMS.domain.solution.Solution;
import com.example.LMS.repository.SolutionRepository;
import com.example.LMS.repository.TaskRepository;
import com.example.LMS.service.SolutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SolutionServiceImplementation implements SolutionService {

    private final SolutionRepository solutionRepository;

    private final TaskRepository taskRepository;

    @Transactional(readOnly = true)
    @Override
    public Solution getSolution(Long userId, Long taskId) {
        Long userTaskId = taskRepository.findUserTaskId(userId, taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
        return solutionRepository.findSolution(userTaskId)
                .orElseThrow(() -> new ResourceNotFoundException("Solution not found"));
    }

    @Transactional
    @Override
    public void createSolution(Solution solution) {
        Long userTaskId = taskRepository.findUserTaskId(solution.getUserId(), solution.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
        solutionRepository.create(solution, userTaskId);
    }

    @Transactional
    @Override
    public void updateSolution(Solution solution) {
        Long userTaskId = taskRepository.findUserTaskId(solution.getUserId(), solution.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
        solutionRepository.update(solution, userTaskId);
    }

}
