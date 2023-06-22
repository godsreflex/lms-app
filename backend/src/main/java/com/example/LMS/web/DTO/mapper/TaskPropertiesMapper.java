package com.example.LMS.web.DTO.mapper;

import com.example.LMS.domain.task.TaskProperties;
import com.example.LMS.web.DTO.task.TaskPropertiesDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskPropertiesMapper {

    TaskProperties toEntity(TaskPropertiesDTO taskPropertiesDTO);

    TaskPropertiesDTO toDTO(TaskProperties taskProperties);

}
