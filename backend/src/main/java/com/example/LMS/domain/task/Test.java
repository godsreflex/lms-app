package com.example.LMS.domain.task;

import lombok.Data;

@Data
public class Test {

    private Long id;
    private String input;
    private String output;

    @Override
    public boolean equals(Object object) {
        Test test = (Test) object;
        if ((this.getInput().equals(test.getInput()))
                && (this.getOutput().equals(test.getOutput()))) return true;
        return false;
    }

}
