package com.foodie.monolith.service;

import com.foodie.monolith.data.NewUserInformation;
import com.foodie.monolith.data.UserInformation;
import com.foodie.monolith.model.User;
import com.foodie.monolith.payload.request.SignupRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<User> getUsers();

    public List<UserInformation> getAllUserInformation() throws Exception;

    public UserInformation getUserById(String foodieCookie, Long userId);

//    public String createUser(NewUserInformation newUserInformation);

    public String registerUser(SignupRequest signupRequest);

    public String updateUser(Long userId, User updateUser);

    public String setUserActive(String foodieCookie, UserInformation updateUser);

    public String deleteUser(Long userId);

    public UserInformation login(User user);
}