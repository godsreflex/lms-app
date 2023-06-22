package com.example.LMS.web.DTO.mapper;

import com.example.LMS.domain.verification.VerificationResult;
import com.example.LMS.web.DTO.verification.VerificationResultDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VerificationMapper {

    List<VerificationResult> toEntity(List<VerificationResultDTO> verificationResultsDTO);

    List<VerificationResultDTO> toDTO(List<VerificationResult> verificationResults);

}
