package org.userservice.service;

import org.userservice.model.UserDtoIn;
import org.userservice.model.UserDtoOut;

import java.util.List;

public interface UserService {
    UserDtoOut createUser(UserDtoIn userDto);
    UserDtoOut getById(Long id);
    List<UserDtoOut> getAllUsers();
    UserDtoOut updateUser(Long id, UserDtoIn userDto);
    void deleteUser(Long id);
}
