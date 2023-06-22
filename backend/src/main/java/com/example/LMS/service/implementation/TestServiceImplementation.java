package com.example.LMS.service.implementation;

import com.example.LMS.domain.task.Test;
import com.example.LMS.repository.TestRepository;
import com.example.LMS.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImplementation implements TestService {

    private final TestRepository testRepository;

    @Transactional
    @Override
    public void addTests(List<Test> tests, Long taskId) {
        List<Test> currentTests = getTests(taskId);
        for (Test testToAdd: tests) {
            if (!alreadyExists(testToAdd, currentTests)) testRepository.addTest(testToAdd, taskId);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<Test> getTests(Long taskId) {
        return testRepository.findTests(taskId);
    }

    private boolean alreadyExists(Test testToAdd, List<Test> currentTests) {
        for (Test test : currentTests) {
            if (testToAdd.equals(test)) return true;
        }
        return false;
    }

}
