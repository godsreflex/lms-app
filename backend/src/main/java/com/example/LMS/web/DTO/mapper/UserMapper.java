package com.example.LMS.web.DTO.mapper;

import com.example.LMS.domain.user.User;
import com.example.LMS.web.DTO.user.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);

    List<UserDTO> toDTO(List<User> users);

}
