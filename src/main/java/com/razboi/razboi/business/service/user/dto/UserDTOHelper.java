package com.razboi.razboi.business.service.user.dto;


import com.razboi.razboi.persistence.user.entity.User;

public class UserDTOHelper {

    public static User toEntity(UserDTO userDTO) {
        User user = new User();

        user.setID(userDTO.getID());
        user.setUsername(userDTO.getUsername());
        return user;
    }

    public static UserDTO fromEntity(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUsername(user.getUsername());
        userDTO.setID(user.getID());
        return userDTO;
    }
}
