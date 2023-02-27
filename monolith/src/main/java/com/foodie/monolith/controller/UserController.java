package com.foodie.monolith.controller;

import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.User;
import com.foodie.monolith.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

//
    @GetMapping(value = "/users")
    public ResponseEntity<List<User>> getUsers() throws UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}