package com.example.LMS.repository.mapper;

import com.example.LMS.domain.task.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TestRowMapper {

    public static List<Test> mapRows(ResultSet resultSet) throws SQLException {
        List<Test> tests = new ArrayList<>();
        while (resultSet.next()) {
            Test test = new Test();
            test.setId(resultSet.getLong("tests_id"));
            test.setInput(resultSet.getString("tests_input"));
            test.setOutput(resultSet.getString("tests_output"));
            tests.add(test);
        }
        return tests;
    }

}
