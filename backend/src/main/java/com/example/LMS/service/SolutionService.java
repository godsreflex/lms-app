package com.example.LMS.service;

import com.example.LMS.domain.solution.Solution;

public interface SolutionService {

    Solution getSolution(Long userId, Long taskId);

    void createSolution(Solution solution);

    void updateSolution(Solution solution);

}
