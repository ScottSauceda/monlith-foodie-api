package com.foodie.monolith.controller;

import com.foodie.monolith.data.UserInformation;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.model.User;
import com.foodie.monolith.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
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

    @GetMapping("/allUserInformation")
    public ResponseEntity<List<UserInformation>> getAllUserInformation() throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUserInformation());
        } catch(Exception exception){
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserInformation> getUserById(@PathVariable Integer userId) throws UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUserById(userId));
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createUser(@RequestBody User newUser) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(newUser));
        } catch(Exception exception){
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping(value = "/update/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Integer userId, @RequestBody User updateUser) throws UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(userId, updateUser));
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer userId) throws UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(userId));
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}