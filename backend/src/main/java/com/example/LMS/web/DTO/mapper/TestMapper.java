package com.example.LMS.web.DTO.mapper;

import com.example.LMS.domain.task.Test;
import com.example.LMS.web.DTO.task.TestDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TestMapper {

    List<Test> toEntity(List<TestDTO> tests);

    List<TestDTO> toDTO(List<Test> tests);

}
