package com.foodie.monolith.service;

import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.User;
import com.foodie.monolith.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

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

}