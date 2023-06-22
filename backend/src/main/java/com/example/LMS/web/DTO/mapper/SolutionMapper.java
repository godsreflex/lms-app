package com.example.LMS.web.DTO.mapper;

import com.example.LMS.domain.solution.Solution;
import com.example.LMS.web.DTO.solution.SolutionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SolutionMapper {

    SolutionDTO toDTO(Solution solution);

    Solution toEntity(SolutionDTO solutionDTO);

}
