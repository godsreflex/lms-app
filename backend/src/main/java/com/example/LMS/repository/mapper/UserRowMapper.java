package com.example.LMS.repository.mapper;

import com.example.LMS.domain.user.Role;
import com.example.LMS.domain.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRowMapper {

    public static User mapRow(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getLong("user_id"));
            user.setName(resultSet.getString("user_name"));
            user.setUsername(resultSet.getString("user_username"));
            user.setPassword(resultSet.getString("user_password"));
            user.setRole(Role.valueOf(resultSet.getString("user_role")));
            return user;
        }
        return null;
    }

    public static List<User> mapRows(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getLong("user_id"));
            if (!resultSet.wasNull()) {
                user.setName(resultSet.getString("user_name"));
                user.setUsername(resultSet.getString("user_username"));
                user.setPassword(resultSet.getString("user_password"));
                user.setRole(Role.valueOf(resultSet.getString("user_role")));
                users.add(user);
            }
        }
        return users;
    }

}
