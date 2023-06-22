package com.example.LMS.service;

import com.example.LMS.domain.task.Test;

import java.util.List;

public interface TestService {

    void addTests(List<Test> tests, Long taskId);

    List<Test> getTests(Long taskId);

}
