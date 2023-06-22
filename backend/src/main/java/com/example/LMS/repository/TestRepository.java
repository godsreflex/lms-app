package com.example.LMS.repository;

import com.example.LMS.domain.task.Test;

import java.util.List;

public interface TestRepository {

    void addTest(Test test, Long taskId);

    List<Test> findTests(Long taskId);

}
