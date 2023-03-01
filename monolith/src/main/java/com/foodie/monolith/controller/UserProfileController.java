package com.foodie.monolith.controller;

import com.foodie.monolith.exception.RoleNotFoundException;
import com.foodie.monolith.exception.UserNotFoundException;
import com.foodie.monolith.exception.UserProfileNotFoundException;
import com.foodie.monolith.model.Role;
import com.foodie.monolith.model.UserProfile;
import com.foodie.monolith.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    UserProfileService userProfileService;

    @GetMapping(value = "/profiles")
    public ResponseEntity<List<UserProfile>> getProfiles() throws UserProfileNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userProfileService.getUserProfiles());
        } catch(UserProfileNotFoundException profileNotFoundException){
            return new ResponseEntity(profileNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(value = "/{userId}")
    public ResponseEntity<Optional<UserProfile>> getUserProfileByUserId(@PathVariable Integer userId) throws UserProfileNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userProfileService.getUserProfileByUserId(userId));
        } catch(UserProfileNotFoundException userProfileNotFoundException){
            return new ResponseEntity(userProfileNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createUserProfile(@RequestBody UserProfile newUserProfile) throws UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userProfileService.createUserProfile(newUserProfile));
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/update/{userId}")
    public ResponseEntity<String> updateUserProfile(@PathVariable Integer userId, @RequestBody UserProfile updateUserProfile) throws UserProfileNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userProfileService.updateUserProfile(userId, updateUserProfile));
        } catch(UserProfileNotFoundException userProfileNotFoundException){
            return new ResponseEntity(userProfileNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/delete/{userId}")
    public ResponseEntity<String> deleteUserProfile(@PathVariable Integer userId) throws UserProfileNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userProfileService.deleteUserProfile(userId));
        } catch(UserProfileNotFoundException userProfileNotFoundException){
            return new ResponseEntity(userProfileNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}