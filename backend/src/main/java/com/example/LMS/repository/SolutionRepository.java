package com.example.LMS.repository;

import com.example.LMS.domain.solution.Solution;

import java.util.Optional;

public interface SolutionRepository {

    Optional<Solution> findSolution(Long userTaskId);

    void create(Solution solution, Long userTaskId);

    void update(Solution solution, Long userTaskId);

}
