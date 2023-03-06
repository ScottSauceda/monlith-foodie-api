package com.foodie.monolith.service;

import com.foodie.monolith.data.UserInformation;
import com.foodie.monolith.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<User> getUsers();

    public List<UserInformation> getAllUserInformation() throws Exception;

    public UserInformation getUserById(Integer userId);
//
    public String createUser(User newUser);
//
    public String updateUser(Integer userId, User updateUser);
//
    public String deleteUser(Integer userId);
}