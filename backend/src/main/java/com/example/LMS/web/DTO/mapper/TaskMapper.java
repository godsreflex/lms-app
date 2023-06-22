package com.example.LMS.web.DTO.mapper;

import com.example.LMS.domain.task.Task;
import com.example.LMS.web.DTO.task.TaskDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDTO toDTO(Task task);

    List<TaskDTO> toDTO(List<Task> tasks);

    Task toEntity(TaskDTO taskDTO);

}
