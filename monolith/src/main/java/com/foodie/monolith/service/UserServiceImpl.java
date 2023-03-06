package com.foodie.monolith.service;

import com.foodie.monolith.data.UserInformation;
import com.foodie.monolith.exception.LocationNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.Location;
import com.foodie.monolith.model.User;
import com.foodie.monolith.model.UserProfile;
import com.foodie.monolith.repository.UserProfileRepository;
import com.foodie.monolith.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Transactional
    public List<User> getUsers() throws UserNotFoundException {
        List<User>  users = new ArrayList<User>();
        if(userRepository.findAll().isEmpty()){
            throw new UserNotFoundException("No Users to return");
        } else {
            List<User> dbUsers = userRepository.findAll();
            for(User user: dbUsers){
                users.add(user);
            }
            return users;
        }
    }

    @Transactional
    public List<UserInformation> getAllUserInformation() throws Exception {
        List<UserInformation> userInformation = new ArrayList<>();

        if(userProfileRepository.findAll().isEmpty()){
            throw new Exception("Something went wrong, please try again.");
        } else {
            for(UserProfile dbUser: userProfileRepository.findAll()){
                userInformation.add(getUserInformation(dbUser.getUsersId()));
            }
        }

        return userInformation;
    }

    @Transactional
    public UserInformation getUserById(Integer userId) throws UserNotFoundException {
        UserInformation userInformation = null;
        if(userRepository.findById(userId).isEmpty()){
            throw new UserNotFoundException("Location with Id: " + userId + " does not exists. Please try again.");
        } else {

            userInformation = getUserInformation(userId);
        }
        return userInformation;
    }

    @Transactional
    public String createUser(User newUser) {
        User savedUser = null;
        savedUser = userRepository.saveAndFlush(newUser);

        if(userRepository.findById(savedUser.getUserId()).isEmpty()){
            return "Something went wrong. Please try again";
        } else {
            return "New User created with Id: " + savedUser.getUserId();
        }
    }

    @Transactional
    public String updateUser(Integer userId, User updateUser) throws UserNotFoundException {
        User dbUser = userRepository.findById(userId).orElse(null);;

        if(dbUser == null){
            throw new UserNotFoundException("User with Id: " + userId + "does not exists. Please try again.");
        } else {
            dbUser.setPassword(updateUser.getPassword());

            return "User has been updated successfully";
        }
    }

    @Transactional
    public String deleteUser(Integer userId) throws UserNotFoundException {
        User dbUser = userRepository.findById(userId).orElse(null);

        if(dbUser == null){
            throw new UserNotFoundException("User with Id: " + userId + " does not exists. Please try again.");
        } else {
            userRepository.delete(dbUser);
            return "User has been deleted successfully";
        }
    }

    @Transactional
    public UserInformation getUserInformation(Integer userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.get();

        Optional<UserProfile> optionalUserProfile = userProfileRepository.findById(userId);
        UserProfile userProfile = optionalUserProfile.get();

        UserInformation userInformation = new UserInformation();

        userInformation.setUsersId(user.getUserId());
        userInformation.setUserName(user.getUsername());
        userInformation.setIsActive(user.isActive());

        userInformation.setFirstName(userProfile.getFirstName());
        userInformation.setLastName(userProfile.getLastName());
        userInformation.setEmail(userProfile.getEmail());
        userInformation.setPhone(userProfile.getPhone());

        return userInformation;

    }

}