package com.example.LMS.repository.mapper;

import com.example.LMS.domain.verification.VerificationResult;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VerificationResultRowMapper {

    public static List<VerificationResult> mapRows(ResultSet resultSet) throws SQLException {
        List<VerificationResult> results = new ArrayList<>();
        while (resultSet.next()) {
            VerificationResult result = new VerificationResult();
            result.setReceived(resultSet.getString("verif_results_received"));
            result.setExpected(resultSet.getString("verif_results_expected"));
            result.setResult(resultSet.getBoolean("verif_results_result"));
            result.setTestId(resultSet.getLong("verif_results_test_id"));
            results.add(result);
        }
        return results;
    }

}
