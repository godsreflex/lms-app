package com.example.LMS.repository.mapper;

import com.example.LMS.domain.solution.Solution;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SolutionRowMapper {

    public static Solution mapRow(ResultSet resultSet) throws SQLException {
        Solution solution = new Solution();
        if (resultSet.next()) {
            solution.setPostingDate(resultSet.getTimestamp("user_solutions_posting_date").toLocalDateTime());
            solution.setText(resultSet.getString("user_solutions_text"));
            return solution;
        }
        return null;
    }

}
